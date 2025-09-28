package dev.amble.stargate.api.v3.behavior.client;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientMilkyWayState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import net.minecraft.client.model.ModelPart;

public class ClientMilkyWayBehavior extends ClientGenericGateBehavior {

    @Override
    public boolean shouldOverride(Stargate stargate) {
        return stargate.stateOrNull(ClientMilkyWayState.state) instanceof ClientMilkyWayState;
    }

    @Override
    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        BasicGateStates<?> state = stargate.getCurrentState();

        boolean visible = state != null && state.gateState() != BasicGateStates.StateType.CLOSED;
        int locked = state instanceof BasicGateStates.Closed closed ? closed.locked : -1;

        ModelPart[] chevrons = renderer.chevrons;

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i < locked;
        }
    }
}
