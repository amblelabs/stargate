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

    Minecraft minecraft = Minecraft.getInstance();
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
        float g = 512.0F;
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferBuilder.addVertex(0.0F, y, 0.0F);

        for(int i = -180; i <= 180; i += 45) {
            bufferBuilder.addVertex(f * Mth.cos((float)i * ((float)Math.PI / 180F)), y, 512.0F * Mth.sin((float)i * ((float)Math.PI / 180F)));
        }

        return bufferBuilder.buildOrThrow();
    }

    private MeshData drawStars(Tesselator tesselator) {
        RandomSource randomSource = RandomSource.create(10842L);
        int i = 1500;
        float f = 100.0F;
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
        if (!this.minecraft.level.effects().isFoggyAt(Mth.floor(camera.getPosition().x), Mth.floor(camera.getPosition().y)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
            FogType fogType = camera.getFluidInCamera();
            if (fogType != FogType.POWDER_SNOW && fogType != FogType.LAVA && !this.doesMobEffectBlockSky(camera)) {
                PoseStack poseStack = new PoseStack();
                poseStack.mulPose(frustumMatrix);
                Vec3 vec3 = minecraft.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
                float f = (float)vec3.x;
                float g = (float)vec3.y;
                float h = (float)vec3.z;
                FogRenderer.levelFogColor();
                Tesselator tesselator = Tesselator.getInstance();
                RenderSystem.depthMask(false);
                RenderSystem.setShaderColor(f, g, h, 1.0F);
                ShaderInstance shaderInstance = RenderSystem.getShader();
                this.skyBuffer.bind();
                this.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
                VertexBuffer.unbind();
                RenderSystem.enableBlend();
                float[] fs = minecraft.level.effects().getSunriseColor(minecraft.level.getTimeOfDay(partialTick), partialTick);
                if (fs != null) {
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    poseStack.pushPose();
                    poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                    float i = Mth.sin(minecraft.level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                    poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(i));
                    poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                    float j = fs[0];
                    float k = fs[1];
                    float l = fs[2];
                    Matrix4f matrix4f = poseStack.last().pose();
                    BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferBuilder.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(j, k, l, fs[3]);
                    int m = 16;

                    for(int n = 0; n <= 16; ++n) {
                        float o = (float)n * ((float)Math.PI * 2F) / 16.0F;
                        float p = Mth.sin(o);
                        float q = Mth.cos(o);
                        bufferBuilder.addVertex(matrix4f, p * 120.0F, q * 120.0F, -q * 40.0F * fs[3]).setColor(fs[0], fs[1], fs[2], 0.0F);
                    }

                    BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
                    poseStack.popPose();
                }

                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                poseStack.pushPose();
                float i = 1.0F - minecraft.level.getRainLevel(partialTick);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, i);
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(minecraft.level.getTimeOfDay(partialTick) * 360.0F));
                Matrix4f matrix4f2 = poseStack.last().pose();
                float k = 30.0F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SUN_LOCATION);
                BufferBuilder bufferBuilder2 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferBuilder2.addVertex(matrix4f2, -k, 100.0F, -k).setUv(0.0F, 0.0F);
                bufferBuilder2.addVertex(matrix4f2, k, 100.0F, -k).setUv(1.0F, 0.0F);
                bufferBuilder2.addVertex(matrix4f2, k, 100.0F, k).setUv(1.0F, 1.0F);
                bufferBuilder2.addVertex(matrix4f2, -k, 100.0F, k).setUv(0.0F, 1.0F);
                BufferUploader.drawWithShader(bufferBuilder2.buildOrThrow());

                float[] moonXOffsets = {-0, 25.0f, -28.0f};
                float[] moonYOffsets = {-0, -10.0f, -12.5f};
                float[] moonScales = {1, 0.5f, 0.75f};

                for (int j = 0; j < 3; j++) {

                    float xOffset = moonXOffsets[j];
                    float yOffset = moonYOffsets[j];

                    k = 20.0F * moonScales[j];
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int r = minecraft.level.getMoonPhase();
                    int s = r % 4;
                    int m = r / 4 % 2;
                    float t = (float)(s + 0) / 4.0F;
                    float o = (float)(m + 0) / 2.0F;
                    float p = (float)(s + 1) / 4.0F;
                    float q = (float)(m + 1) / 2.0F;
                    bufferBuilder2 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferBuilder2.addVertex(matrix4f2, -k-xOffset, -100.0F, k-yOffset).setUv(p, q);
                    bufferBuilder2.addVertex(matrix4f2, k-xOffset, -100.0F, k-yOffset).setUv(t, q);
                    bufferBuilder2.addVertex(matrix4f2, k-xOffset, -100.0F, -k-yOffset).setUv(t, o);
                    bufferBuilder2.addVertex(matrix4f2, -k-xOffset, -100.0F, -k-yOffset).setUv(p, o);
                    BufferUploader.drawWithShader(bufferBuilder2.buildOrThrow());
                }


                float u = minecraft.level.getStarBrightness(partialTick) * i;
                if (u > 0.0F) {
                    RenderSystem.setShaderColor(u, u, u, u);
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
                double d = this.minecraft.player.getEyePosition(partialTick).y - minecraft.level.getLevelData().getHorizonHeight(minecraft.level);
                if (d < (double)0.0F) {
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
