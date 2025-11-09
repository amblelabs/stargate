package dev.amble.stargate.client.api.behavior.stargate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.stargate.PegasusState;
import dev.amble.stargate.client.api.state.stargate.ClientPegasusState;

public class ClientPegasusBehavior extends ClientAbstractStargateBehavior<ClientPegasusState> {

    public ClientPegasusBehavior() {
        super(PegasusState.class, ClientPegasusState.class);
    }

    @Override
    protected ClientPegasusState createClientState(Stargate stargate) {
        return new ClientPegasusState();
    }
}
