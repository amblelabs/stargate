package dev.amble.stargate.api.v3.state.stargate.client;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.Identifier;

public class ClientGenericGateState implements TState<ClientGenericGateState> {

    public static final Type<ClientGenericGateState> state = new Type<>(StargateMod.id("meta/client"));

    public int glyphColor = 0x5c5c73;

    public int portalSize = 32;
    public float portalYOffset = -0.9f;
    public Identifier portalType = StargateMod.id("textures/portal/normal.png");

    public boolean custom;

    public ClientGenericGateState(boolean custom) {
        this.custom = custom;
    }

    @Override
    public Type<ClientGenericGateState> type() {
        return state;
    }
}
