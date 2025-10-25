package dev.amble.stargate.api.behavior.client;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.state.stargate.MilkyWayState;
import dev.amble.stargate.api.state.stargate.client.ClientMilkyWayState;

public class ClientMilkyWayBehavior extends ClientAbstractStargateBehavior<ClientMilkyWayState> implements StargateLoadedEvents {

    public ClientMilkyWayBehavior() {
        super(MilkyWayState.class, ClientMilkyWayState.class);
    }

    @Override
    protected ClientMilkyWayState createClientState(Stargate stargate) {
        return new ClientMilkyWayState();
    }
}
