package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.state.stargate.DestinyState;

public class ClientDestinyState extends ClientAbstractStargateState {

    public ClientDestinyState() {
        super(DestinyState.ID);

        this.portalType = StargateMod.id("textures/portal/white.png");
    }
}
