package dev.amble.stargate.client.api.behavior.stargate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.stargate.DestinyState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.state.stargate.ClientDestinyState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

public class ClientDestinyBehavior extends ClientAbstractStargateBehavior.Spinning<ClientDestinyState> {

    public ClientDestinyBehavior() {
        super(DestinyState.class, ClientDestinyState.class);
    }

    @Override
    protected ClientDestinyState createClientState(Stargate stargate) {
        return new ClientDestinyState();
    }

    @Override
    protected void preRender(Stargate stargate, ClientDestinyState clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        super.preRender(stargate, clientState, entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);

        ModelPart root = clientState.model().getPart();
        root.roll = renderGlyphs(root.roll, 180f, clientState, matrices, vertexConsumers, stargate, light);
    }
}
