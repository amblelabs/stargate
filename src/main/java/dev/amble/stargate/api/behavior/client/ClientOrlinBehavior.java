package dev.amble.stargate.api.behavior.client;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.OrlinState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.api.state.stargate.client.ClientOrlinState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.EmissionUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import static dev.amble.stargate.client.renderers.StargateBlockEntityRenderer.ORLIN_GATE;

public class ClientOrlinBehavior extends ClientAbstractStargateBehavior<ClientOrlinState> implements StargateLoadedEvents {

    public ClientOrlinBehavior() {
        super(OrlinState.class, ClientOrlinState.class);
    }

    @Override
    protected ClientOrlinState createClientState(Stargate stargate) {
        return new ClientOrlinState();
    }

    @Override
    protected void customRender(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, ClientAbstractStargateState clientState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.5f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        boolean bl = stargate.getGateState().gateState() != GateState.StateType.CLOSED;
        EmissionUtil.render2Layers(ORLIN_GATE, clientState.texture, clientState.emission, bl, matrices, vertexConsumers, light, overlay);
    }
}
