package dev.amble.stargate.api.gates.state.stargate.client;

import dev.amble.stargate.StargateMod;

public class ClientDestinyState extends ClientGenericGateState {

    public ClientDestinyState() {
        super(false);

        this.portalType = StargateMod.id("textures/portal/white.png");
    }
}
