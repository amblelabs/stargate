package dev.amble.stargate.client.util;

import dev.amble.stargate.client.renderers.StargateRenderLayers;
import dev.amble.stargate.compat.Compat;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EmissionUtil {

    public static void render2Layers(SinglePartEntityModel<?> model, Identifier baseTexture, Identifier emission, boolean hasEmission, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (hasEmission && Compat.hasIris())
            model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);

        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(baseTexture)), light, overlay, 1, 1, 1, 1);

        if (hasEmission && !Compat.hasIris())
            model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);
    }
}
