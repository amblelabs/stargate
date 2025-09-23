package dev.amble.stargate.api.v3;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractGateKernel<S extends GateStates<S>> {

    private final @NotNull S states;

    public AbstractGateKernel(@NotNull S states) {
        this.states = states;
    }

    private @NotNull GateState<S> state;

    public void setState(@NotNull GateState<S> state) {
        this.state = state;
    }

    public boolean isClient() {

    }

    public @NotNull GateState<S> getState() {
        return state;
    }
}
