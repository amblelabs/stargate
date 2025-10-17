package dev.amble.stargate.api.state.stargate.client;

public class ClientOrlinState extends ClientAbstractStargateState {

    public ClientOrlinState() {
        super(true);

        this.portalSize = 14;
        this.portalYOffset = 1f;
    }
}
