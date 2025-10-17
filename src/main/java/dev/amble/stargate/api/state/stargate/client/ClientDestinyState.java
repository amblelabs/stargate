package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.StargateMod;

public class ClientDestinyState extends ClientAbstractStargateState {

    public ClientDestinyState() {
        super(false);

        this.portalType = StargateMod.id("textures/portal/white.png");
    }
}
