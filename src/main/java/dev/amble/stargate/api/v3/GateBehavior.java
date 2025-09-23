package dev.amble.stargate.api.v3;

public interface GateBehavior<T extends GateState<T>> {

    GateState.Type<T> type();

    default void init(GateKernel kernel, T state) { }
    default void tick(GateKernel kernel, T state) { }
    default void finish(GateKernel kernel, T state) { }
}
