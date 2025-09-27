package dev.amble.stargate.api.v3.state;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.Identifier;

public class ClientGateState implements TState<ClientGateState> {

    public static final Type<ClientGateState> state = new Type<>(StargateMod.id("meta/client"));

    public final Identifier texture;
    public final Identifier emission;

    public ClientGateState(Identifier texture, Identifier emission) {
        this.texture = texture;
        this.emission = emission;
    }

    @Override
    public Type<ClientGateState> type() {
        return state;
    }
}
