package dev.amble.stargate.api.v3.state.client;

import dev.amble.stargate.StargateMod;

public class ClientDestinyState extends ClientGenericGateState {

    public ClientDestinyState() {
        super(false);

        this.portalType = StargateMod.id("textures/portal/white.png");
    }
}
