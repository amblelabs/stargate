package dev.amble.stargate.api.v3;

public interface GateBehaviors<S extends GateStates<S>> {

    <T extends GateState<T, S>> GateBehavior<S, T> fromState(T state);
}
