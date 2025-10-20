package dev.amble.stargate.api.behavior.stargate.client;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.stargate.DestinyState;
import dev.amble.stargate.api.state.stargate.client.ClientDestinyState;

public class ClientDestinyBehavior extends ClientAbstractStargateBehavior<ClientDestinyState> {

    public ClientDestinyBehavior() {
        super(DestinyState.class, ClientDestinyState.class);
    }

    @Override
    protected ClientDestinyState createClientState(Stargate stargate) {
        return new ClientDestinyState();
    }
}
