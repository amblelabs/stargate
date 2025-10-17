package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.Identifier;

public abstract class ClientAbstractStargateState implements TState<ClientAbstractStargateState> {

    public static final Type<ClientAbstractStargateState> state = new Type<>(StargateMod.id("meta/client"));

    public int glyphColor = 0x5c5c73;

    public int portalSize = 32;
    public float portalYOffset = -0.9f;
    public Identifier portalType = StargateMod.id("textures/portal/normal.png");

    @Override
    public Type<ClientAbstractStargateState> type() {
        return state;
    }
}
