package dev.amble.stargate.api.v3.state.client;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.TState;
import net.minecraft.entity.AnimationState;

public class ClientIrisState implements TState<ClientIrisState> {

    public static final Type<ClientIrisState> state = new Type<>(StargateMod.id("iris/client"));

    public AnimationState CLOSE_STATE = new AnimationState();
    public AnimationState OPEN_STATE = new AnimationState();
    public boolean stopOpening;

    @Override
    public Type<ClientIrisState> type() {
        return state;
    }
}
