package dev.amblelabs.stargate.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class GlyphRenderLayer<T extends StargateBlockEntity> extends GeoRenderLayer<T> {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890";
    private static final float ANGLE_STEP = (float) (2f * Math.PI / ALPHABET.length());
    private static final float RADIUS = 155f; // i have no idea why that is

    private static final Minecraft mc = Minecraft.getInstance();

    public GlyphRenderLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0, 3.5, 0.21);
        poseStack.scale(0.02f, -0.02f, 0.02f);

        for (int i = 0; i < ALPHABET.length(); i++) {
            String character = String.valueOf(ALPHABET.charAt(i));
            float theta = i * ANGLE_STEP;

            poseStack.pushPose();

            // ACTUALLY rotate the characters around the center pivot
            poseStack.mulPose(Axis.ZP.rotation(theta));

            // Move the first character to the top chevron
            poseStack.mulPose(Axis.ZP.rotationDegrees(-89.5f));

            // TODO This rotates the symbols with the ring, implement animation timing
            // poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotateGlyph / 10f));

            poseStack.translate(RADIUS, 0, 0);

            float charWidth = mc.font.width(character);
            float charHeight = 8;

            // Reset pivot position to the center of the character, and rotate it around its axis to point its ass to the center
            poseStack.translate(-charWidth / 2f, -charHeight / 2f, 0);
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90));

            mc.font.drawInBatch(
                    Component.literal(character),
                    0, 0,
                    Color.ofRGBA(0.18f, 0.18f, 0.18f, 1f).getColor(),
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
