package dev.amble.stargate.api.behavior.stargate.client;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.client.ClientMilkyWayState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import net.minecraft.client.model.ModelPart;

public class ClientMilkyWayBehavior extends ClientAbstractStargateBehavior<ClientMilkyWayState> implements StargateLoadedEvents {

    public ClientMilkyWayBehavior() {
        super(ClientMilkyWayState.class, ClientMilkyWayState.state, ClientMilkyWayState::new);
    }

    @Override
    public void onLoaded(Stargate stargate) {
        if (!stargate.isClient()) return;
        stargate.addState(new ClientMilkyWayState());
    }

    @Override
    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        GateState<?> state = stargate.getGateState();

        boolean visible = state != null && state.gateState() != GateState.StateType.CLOSED;
        int locked = state instanceof GateState.Closed closed ? closed.locked : -1;

        ModelPart[] chevrons = renderer.chevrons;

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i < locked;
        }
    }
}
