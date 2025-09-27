package dev.amble.stargate.api.v3.event.render;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public record StargateRenderEvent(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) implements TEvent.Notify<StargateRenderEvents> {

    @Override
    public TEvents.BaseType<StargateRenderEvents> type() {
        return StargateRenderEvents.event;
    }

    @Override
    public void handle(StargateRenderEvents handler) throws StateResolveError {
        handler.render(stargate, entity, renderer, matrices, vertexConsumers, light, overlay, tickDelta);
    }
}
