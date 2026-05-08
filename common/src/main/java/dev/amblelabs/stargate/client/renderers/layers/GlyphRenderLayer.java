package dev.amblelabs.stargate.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class GlyphRenderLayer<T extends StargateBlockEntity> extends GeoRenderLayer<T> {

    Minecraft mc = Minecraft.getInstance();

    public GlyphRenderLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        String text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        float angleStep = (float) (2 * Math.PI / text.length());
        float radius = 155f;

        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
        poseStack.translate(0, 3.5, 0.2);
        poseStack.scale(0.02f, -0.02f, 0.02f);

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            float theta = i * angleStep;

            poseStack.pushPose();

            poseStack.mulPose(com.mojang.math.Axis.ZP.rotation(theta));
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-89.5f));

            poseStack.translate(radius, 0, 0);

            float charWidth = mc.font.width(character);
            float charHeight = 8;

            poseStack.translate(-charWidth / 2f, -charHeight / 2f, 0);
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90));

            mc.font.drawInBatch(
                    character,
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
