package dev.amble.stargate.client.renderers;

import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class RingBlockEntityRenderer implements BlockEntityRenderer<StargateRingBlockEntity> {

    public RingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(StargateRingBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getBlockSet() != null) {
            BlockState blockState = entity.getBlockSet();
            matrices.push();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(blockState, entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(blockState)), true, MinecraftClient.getInstance().world.getRandom());
            matrices.pop();
        }
    }

}