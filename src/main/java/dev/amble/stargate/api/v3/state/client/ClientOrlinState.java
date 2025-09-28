package dev.amble.stargate.api.v3.state.client;

public class ClientOrlinState extends ClientGenericGateState {

    public ClientOrlinState() {
        super(true);

        this.portalSize = 14;
        this.portalYOffset = 1f;
    }
}
