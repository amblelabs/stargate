package dev.amble.stargate.api.behavior.stargate.client;

import dev.amble.stargate.api.state.stargate.client.ClientPegasusState;

public class ClientPegasusBehavior extends ClientAbstractStargateBehavior<ClientPegasusState> {

    public ClientPegasusBehavior() {
        super(ClientPegasusState.class, ClientPegasusState.state, ClientPegasusState::new);
    }
}
