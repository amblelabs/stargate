package dev.amble.stargate.api.gates.behavior.client;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.state.GateState;
import dev.amble.stargate.api.gates.state.stargate.client.ClientMilkyWayState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import net.minecraft.client.model.ModelPart;

public class ClientMilkyWayBehavior extends ClientGenericGateBehavior {

    @Override
    public boolean shouldOverride(Stargate stargate) {
        return stargate.stateOrNull(ClientMilkyWayState.state) instanceof ClientMilkyWayState;
    }

    @Override
    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        GateState<?> state = stargate.getCurrentState();

        boolean visible = state != null && state.gateState() != GateState.StateType.CLOSED;
        int locked = state instanceof GateState.Closed closed ? closed.locked : -1;

        ModelPart[] chevrons = renderer.chevrons;

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i < locked;
        }
    }
}
