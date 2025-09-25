package dev.amble.stargate.api.v4.example.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.state.TState;

import java.util.function.Supplier;

public final class GateState implements TState<GateState> {

    public static final Type<GateState> state = new Type<>(StargateMod.id("state"));

    private Type<?> currentState;
    private final Supplier<TState<?>> defState;

    public GateState(Stargate stargate, Supplier<TState<?>> defState, Type<?>... types) {
        this.defState = defState;

        for (Type<?> type : types) {
            TState<?> state = stargate.stateOrNull(type);

            if (state != null) {
                this.currentState = state.type();
                return;
            }
        }
    }

    @Override
    public Type<GateState> type() {
        return state;
    }

    public Type<?> currentState() {
        return currentState;
    }

    public void currentState(Type<?> type) {
        currentState = type;
    }

    public TState<?> createDefaultState() {
        return defState.get();
    }
}
