package dev.amble.stargate.api.v3.event.render;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface StargateRenderEvents extends TEvents {

    Type<StargateRenderEvents> event = new Type<>(StargateRenderEvents.class);

    default void render(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) { }

    default void animate(StargateBlockEntity stargateBlockEntity, Stargate stargate, BaseStargateModel model, int age) { }
}
