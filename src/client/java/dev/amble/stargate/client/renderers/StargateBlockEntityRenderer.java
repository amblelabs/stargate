package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.client.api.event.render.StargateRenderEvent;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.portal.PortalRendering;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {

    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) { }

    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Profiler profiler = MinecraftClient.getInstance().getProfiler();
        profiler.push("stargate");

        if (entity.getBlockSet() != null) {
            matrices.push();

            BlockState blockState = entity.getBlockSet();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(blockState,
                    entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(
                            RenderLayers.getBlockLayer(blockState)), true, entity.getWorld().random
            );

            matrices.pop();
        }

        Stargate gate = entity.asGate();
        if (gate == null) return;

        matrices.push();
        TEvents.handle(new StargateRenderEvent(gate, entity, this, profiler, matrices, vertexConsumers, light, overlay, tickDelta));
        matrices.pop();

        if (gate.getGateState().gateState() != GateState.StateType.CLOSED) PortalRendering.QUEUE.add(entity);

        profiler.pop();
    }
}