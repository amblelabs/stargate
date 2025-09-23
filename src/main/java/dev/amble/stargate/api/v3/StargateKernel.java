package dev.amble.stargate.api.v3;

import org.jetbrains.annotations.NotNull;

public class StargateKernel<S extends GateStates<S>, B extends GateBehaviors<S>> extends AbstractGateKernel<S> {

    private final @NotNull B behaviors;
    private @NotNull GateBehavior<S, ? extends GateState<S>> behavior;

    public StargateKernel(@NotNull S states, @NotNull B behaviors) {
        super(states);

        this.behaviors = behaviors;
        this.setState(states.getDefault());
    }

    @Override
    public void setState(@NotNull GateState<S> state) {
        super.setState(state);

        this.behavior.internal$finish(this);
        this.behavior = this.behaviors.fromState(state);
        this.behavior.internal$init(this);
    }
}
