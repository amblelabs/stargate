package dev.amble.stargate.api.gates.state.stargate.client;

public class ClientOrlinState extends ClientGenericGateState {

    public ClientOrlinState() {
        super(true);

        this.portalSize = 14;
        this.portalYOffset = 1f;
    }
}
