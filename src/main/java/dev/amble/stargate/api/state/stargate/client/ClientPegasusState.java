package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.api.state.stargate.PegasusState;

public class ClientPegasusState extends ClientAbstractStargateState {

    public ClientPegasusState() {
        super(PegasusState.ID);

        this.glyphColor = 0xffffff;
    }
}
