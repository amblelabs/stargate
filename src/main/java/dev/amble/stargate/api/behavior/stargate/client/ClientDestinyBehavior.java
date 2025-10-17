package dev.amble.stargate.api.behavior.stargate.client;

import dev.amble.stargate.api.state.stargate.client.ClientDestinyState;

public class ClientDestinyBehavior extends ClientAbstractStargateBehavior<ClientDestinyState> {

    public ClientDestinyBehavior() {
        super(ClientDestinyState.state, ClientDestinyState::new);
    }
}
