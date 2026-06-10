package dev.amblelabs.stargate.client.renderers.skybox;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AbydosSkyRenderer implements IClientXplatAbstractions.SkyRenderer {

    private static final ResourceLocation MOON_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");

    private static final float[] MOON_X_OFFSETS = {-0, 25.0f, -28.0f};
    private static final float[] MOON_Y_OFFSETS = {-0, -10.0f, -12.5f};
    private static final float[] MOON_SCALES = {1, 0.5f, 0.75f};

    private final Minecraft minecraft = Minecraft.getInstance();
    @Nullable
    private VertexBuffer starBuffer;
    @Nullable
    private VertexBuffer skyBuffer;
    @Nullable
    private VertexBuffer darkBuffer ;

    private void createStars() {
        if (this.starBuffer != null) {
            this.starBuffer.close();
        }

        this.starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.starBuffer.bind();
        this.starBuffer.upload(this.drawStars(Tesselator.getInstance()));
        VertexBuffer.unbind();
    }

    private void createDarkSky() {
        if (this.darkBuffer != null) {
            this.darkBuffer.close();
        }

        this.darkBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.darkBuffer.bind();
        this.darkBuffer.upload(buildSkyDisc(Tesselator.getInstance(), -16.0F));
        VertexBuffer.unbind();
    }

    private void createLightSky() {
        if (this.skyBuffer != null) {
            this.skyBuffer.close();
        }

        this.skyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.skyBuffer.bind();
        this.skyBuffer.upload(buildSkyDisc(Tesselator.getInstance(), 16.0F));
        VertexBuffer.unbind();
    }

    private static MeshData buildSkyDisc(Tesselator tesselator, float y) {
        float f = Math.signum(y) * 512.0F;
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferBuilder.addVertex(0.0F, y, 0.0F);

        for(int i = -180; i <= 180; i += 45) {
            bufferBuilder.addVertex(f * Mth.cos((float)i * ((float)Math.PI / 180F)), y, 512.0F * Mth.sin((float)i * ((float)Math.PI / 180F)));
        }

        return bufferBuilder.buildOrThrow();
    }

    private MeshData drawStars(Tesselator tesselator) {
        RandomSource randomSource = RandomSource.create(10842L);
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for(int j = 0; j < 1500; ++j) {
            float g = randomSource.nextFloat() * 2.0F - 1.0F;
            float h = randomSource.nextFloat() * 2.0F - 1.0F;
            float k = randomSource.nextFloat() * 2.0F - 1.0F;
            float l = 0.15F + randomSource.nextFloat() * 0.1F;
            float m = Mth.lengthSquared(g, h, k);
            if (!(m <= 0.010000001F) && !(m >= 1.0F)) {
                Vector3f vector3f = (new Vector3f(g, h, k)).normalize(100.0F);
                float n = (float)(randomSource.nextDouble() * (double)(float)Math.PI * (double)2.0F);
                Quaternionf quaternionf = (new Quaternionf()).rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), vector3f).rotateZ(n);
                bufferBuilder.addVertex(vector3f.add((new Vector3f(l, -l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add((new Vector3f(l, l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add((new Vector3f(-l, l, 0.0F)).rotate(quaternionf)));
                bufferBuilder.addVertex(vector3f.add((new Vector3f(-l, -l, 0.0F)).rotate(quaternionf)));
            }
        }

        return bufferBuilder.buildOrThrow();
    }

    public void renderSky(ClientLevel level, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera) {
        if (starBuffer == null) createStars();
        if (skyBuffer == null) createLightSky();
        if (darkBuffer == null) createDarkSky();
        if (!level.effects().isFoggyAt(Mth.floor(camera.getPosition().x), Mth.floor(camera.getPosition().y)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
            FogType fogType = camera.getFluidInCamera();
            if (fogType != FogType.POWDER_SNOW && fogType != FogType.LAVA && !this.doesMobEffectBlockSky(camera)) {
                PoseStack poseStack = new PoseStack();
                poseStack.mulPose(frustumMatrix);
                FogRenderer.levelFogColor();
                Tesselator tesselator = Tesselator.getInstance();
                RenderSystem.depthMask(false);
                Vec3 skyColor = level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
                RenderSystem.setShaderColor((float) skyColor.x, (float) skyColor.y, (float) skyColor.z, 1.0F);
                ShaderInstance shaderInstance = RenderSystem.getShader();
                this.skyBuffer.bind();
                this.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
                VertexBuffer.unbind();
                RenderSystem.enableBlend();
                float[] sunriseColor = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);

                //noinspection ConstantValue
                if (sunriseColor != null) {
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    poseStack.pushPose();
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                    float i = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                    poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(i));
                    poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                    Matrix4f matrix4f = poseStack.last().pose();
                    BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferBuilder.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(sunriseColor[0], sunriseColor[1], sunriseColor[2], sunriseColor[3]);

                    for(int n = 0; n <= 16; ++n) {
                        float o = (float)n * ((float)Math.PI * 2F) / 16.0F;
                        float p = Mth.sin(o);
                        float q = Mth.cos(o);
                        bufferBuilder.addVertex(matrix4f, p * 120.0F, q * 120.0F, -q * 40.0F * sunriseColor[3]).setColor(sunriseColor[0], sunriseColor[1], sunriseColor[2], 0.0F);
                    }

                    BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
                    poseStack.popPose();
                }

                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                poseStack.pushPose();

                float starOpacity = 1.0F - level.getRainLevel(partialTick);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, starOpacity);
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));

                Matrix4f matrix4f2 = poseStack.last().pose();
                float starSize = 30.0F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SUN_LOCATION);
                BufferBuilder starBuffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                starBuffer.addVertex(matrix4f2, -starSize, 100.0F, -starSize).setUv(0.0F, 0.0F);
                starBuffer.addVertex(matrix4f2, starSize, 100.0F, -starSize).setUv(1.0F, 0.0F);
                starBuffer.addVertex(matrix4f2, starSize, 100.0F, starSize).setUv(1.0F, 1.0F);
                starBuffer.addVertex(matrix4f2, -starSize, 100.0F, starSize).setUv(0.0F, 1.0F);
                BufferUploader.drawWithShader(starBuffer.buildOrThrow());

                for (int j = 0; j < 3; j++) {

                    float xOffset = MOON_X_OFFSETS[j];
                    float yOffset = MOON_Y_OFFSETS[j];

                    starSize = 20.0F * MOON_SCALES[j];
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int moonPhase = level.getMoonPhase();
                    int s = moonPhase % 4;
                    int m = moonPhase / 4 % 2;
                    float t = (float)(s) / 4.0F;
                    float o = (float)(m) / 2.0F;
                    float p = (float)(s + 1) / 4.0F;
                    float q = (float)(m + 1) / 2.0F;
                    starBuffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    starBuffer.addVertex(matrix4f2, -starSize-xOffset, -100.0F, starSize-yOffset).setUv(p, q);
                    starBuffer.addVertex(matrix4f2, starSize-xOffset, -100.0F, starSize-yOffset).setUv(t, q);
                    starBuffer.addVertex(matrix4f2, starSize-xOffset, -100.0F, -starSize-yOffset).setUv(t, o);
                    starBuffer.addVertex(matrix4f2, -starSize-xOffset, -100.0F, -starSize-yOffset).setUv(p, o);
                    BufferUploader.drawWithShader(starBuffer.buildOrThrow());
                }


                float starBrightness = level.getStarBrightness(partialTick) * starOpacity;
                if (starBrightness > 0.0F) {
                    RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
                    FogRenderer.setupNoFog();
                    this.starBuffer.bind();
                    this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                    VertexBuffer.unbind();
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
                poseStack.popPose();
                RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                double d = (minecraft.player != null ? this.minecraft.player.getEyePosition(partialTick).y : 0) - level.getLevelData().getHorizonHeight(level);
                if (d < 0.0D) {
                    poseStack.pushPose();
                    poseStack.translate(0.0F, 12.0F, 0.0F);
                    this.darkBuffer.bind();
                    this.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
                    VertexBuffer.unbind();
                    poseStack.popPose();
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.depthMask(true);
            }
        }
    }

    private boolean doesMobEffectBlockSky(Camera camera) {
        Entity var3 = camera.getEntity();
        if (!(var3 instanceof LivingEntity livingEntity)) {
            return false;
        } else {
            return livingEntity.hasEffect(MobEffects.BLINDNESS) || livingEntity.hasEffect(MobEffects.DARKNESS);
        }
    }
}
