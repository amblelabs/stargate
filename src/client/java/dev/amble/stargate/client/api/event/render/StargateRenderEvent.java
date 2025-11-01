package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

public record StargateRenderEvent(Stargate stargate, ClientAbstractStargateState clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) implements TEvent.Notify<StargateRenderEvents> {

    public StargateRenderEvent(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        this(stargate, stargate.stateOrNull(ClientAbstractStargateState.state), entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    @Override
    public TEvents.BaseType<StargateRenderEvents> type() {
        return StargateRenderEvents.event;
    }

    @Override
    public void handle(StargateRenderEvents handler) throws StateResolveError {
        profiler.push(handler.getClass().getSimpleName());
        handler.render(stargate, clientState, entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);
        profiler.pop();
    }
}
