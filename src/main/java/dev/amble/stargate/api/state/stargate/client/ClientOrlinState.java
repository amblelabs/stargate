package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.api.state.stargate.OrlinState;

public class ClientOrlinState extends ClientAbstractStargateState {

    public ClientOrlinState() {
        super(OrlinState.ID);

        this.portalSize = 14;
        this.portalYOffset = 1f;
    }
}
