package dev.amble.stargate.api.v3;

public class GateKernel extends DelegateStateHolder<GateKernel> {

    public GateKernel() {
        super(StateRegistry.createMapHolder());
    }

    public void tick() {
        this.forEachState(state -> {
            for (GateBehavior<?> behavior : BehaviorRegistry.get(state.type())) {
                behavior.internal$tick(this);
            }
        });
    }
}
