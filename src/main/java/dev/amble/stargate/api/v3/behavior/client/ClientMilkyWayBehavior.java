package dev.amble.stargate.api.v3.behavior.client;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientMilkyWayState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;

public class ClientMilkyWayBehavior extends ClientGenericGateBehavior {

    @Override
    public boolean shouldOverride(Stargate stargate) {
        return stargate.stateOrNull(ClientMilkyWayState.state) instanceof ClientMilkyWayState;
    }

    @Override
    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        boolean visible = stargate.getCurrentState().gateState() != BasicGateStates.StateType.CLOSED;
        int locked = visible ? -1 : stargate.stateShit(BasicGateStates.Closed.state, closed -> closed.locked, -1);

        for (int i = 0; i < renderer.chevrons.length; i++) {
            renderer.chevrons[i].visible = visible || i < locked;
        }
    }
}
