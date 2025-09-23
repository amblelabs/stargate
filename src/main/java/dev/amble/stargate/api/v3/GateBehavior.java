package dev.amble.stargate.api.v3;

public interface GateBehavior<S extends GateStates<S>, T extends GateState<T, S>> {

    GateState.Type<T> type();

    default void init(AbstractGateKernel<S> kernel, T state) { }
    default void tick(AbstractGateKernel<S> kernel, T state) { }
    default void finish(AbstractGateKernel<S> kernel, T state) { }

    default void internal$init(AbstractGateKernel<S> kernel) {
        init(kernel, state(kernel));
    }

    default void internal$tick(AbstractGateKernel<S> kernel) {
        tick(kernel, state(kernel));
    }

    default void internal$finish(AbstractGateKernel<S> kernel) {
        finish(kernel, state(kernel));
    }

    default T state(AbstractGateKernel<S> kernel) {
        // should be enforced by the type system.
        return (T) kernel.getState();
    }
}
