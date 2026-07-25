package dev.amblelabs.stargate.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.api.mod.CustomGlyph;
import dev.amblelabs.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.mixin.stargate_rendering.FontAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GlyphRenderLayer<T extends StargateBlockEntity> extends GeoRenderLayer<T> {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890";
    private static final float ANGLE_STEP = (float) (2f * Math.PI / ALPHABET.length());
    private static final float RADIUS = 142; // i have no idea why that is

    private static final int COLOR = 0x7d8daf;

    private static final Minecraft mc = Minecraft.getInstance();

    public GlyphRenderLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0, 3.5, 0.21);
        poseStack.scale(0.022f, -0.022f, 0.022f);

        Style style = Style.EMPTY.withFont(StargateAPI.modLoc("milky_way"));

        FontSet set = ((FontAccessor) mc.font).invokeGetFontSet(style.getFont());
        CustomGlyph glyphInfo = (CustomGlyph) set.getGlyphInfo('a', false);

        int charWidth = glyphInfo.stargate$width();
        int charHeight = glyphInfo.stargate$height();

        Direction direction = ((StargateBlockEntityRenderer) this.renderer).getFacing(animatable);

        float blockLight = LightTexture.block(packedLight);
        float skyLight = LightTexture.sky(packedLight);

        float darken = mc.level.getSkyDarken(partialTick);
        float shade = Math.min(mc.level.getShade(direction, true) + 0.2f, 1);

        skyLight *= 1f + (shade - 1) * Math.clamp((darken - 0.2f) / 0.8f, 0, 1);
        skyLight *= darken;

        blockLight *= shade;

        int outBlock = (int) Math.clamp(blockLight, 0, 15);
        int outSky = (int) Math.clamp(skyLight, 0, 15);

        packedLight = LightTexture.pack(outBlock, outSky);

        for (int i = 0; i < ALPHABET.length(); i++) {
            Component character = Component.literal(String.valueOf(ALPHABET.charAt(i)))
                    .withStyle(style);

            float theta = i * ANGLE_STEP;

            poseStack.pushPose();

            // ACTUALLY rotate the characters around the center pivot
            poseStack.mulPose(Axis.ZP.rotation(theta));

            // Move the first character to the top chevron
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));

            // TODO This rotates the symbols with the ring, implement animation timing
            // poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotateGlyph / 10f));

            poseStack.translate(RADIUS, 0, 0);

            // Reset pivot position to the center of the character, and rotate it around its axis to point its ass to the center
            poseStack.translate(-charWidth / 2f, -charHeight / 2f, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90));

            mc.font.drawInBatch(
                    character,
                    0, 0,
                    COLOR | ((int) (0.5 * 255) << 24),
                    false,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    packedLight
            );

            poseStack.translate(0.0F, 0.0F, 0.03F);

            mc.font.drawInBatch(
                    character,
                    1, 0,
                    COLOR,
                    false,
                    poseStack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    packedLight
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
