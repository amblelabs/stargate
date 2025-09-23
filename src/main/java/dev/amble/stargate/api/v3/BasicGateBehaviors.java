package dev.amble.stargate.api.v3;

public abstract class BasicGateBehaviors<S extends GateStates<S>> implements GateBehaviors<S> {

    private final EnumMap<GateState.Type, GateBehavior<S, ? extends GateState<S>>> map = new EnumMap<>(GateState.Type.VALUES, GateBehavior[]::new);

    public BasicGateBehaviors() {
        this.init(map::put);
    }

    protected abstract void init(BehaviorPopulator<S> populator);

    @Override
    public <T extends GateState<S>> GateBehavior<S, T> fromState(T state) {
        return (GateBehavior<S, T>) map.get(state.type());
    }

    @FunctionalInterface
    protected interface BehaviorPopulator<S extends GateStates<S>> {
        void put(GateState.Type type, GateBehavior<S, ? extends GateState<S>> behavior);
    }
}
