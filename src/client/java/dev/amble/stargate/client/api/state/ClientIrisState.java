package dev.amble.stargate.client.api.state;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.TState;
import net.minecraft.entity.AnimationState;

public class ClientIrisState implements TState<ClientIrisState> {

    public static final Type<ClientIrisState> state = new Type<>(StargateMod.id("iris/client"));

    public final AnimationState CLOSE_STATE = new AnimationState();
    public final AnimationState OPEN_STATE = new AnimationState();

    public int ticks;
    public boolean stopOpening;

    @Override
    public Type<ClientIrisState> type() {
        return state;
    }
}
