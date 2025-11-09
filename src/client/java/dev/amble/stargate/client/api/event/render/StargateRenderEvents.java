package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.client.api.state.stargate.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

public interface StargateRenderEvents extends TEvents {

    Type<StargateRenderEvents> event = new Type<>(StargateRenderEvents.class);

    void render(Stargate stargate, ClientAbstractStargateState<?> state, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta);
}
