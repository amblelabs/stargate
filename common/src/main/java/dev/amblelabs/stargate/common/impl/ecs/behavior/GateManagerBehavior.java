package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateGateStateEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.drtheo.ecs.behavior.TBehavior;

public class GateManagerBehavior implements TBehavior {

    private static final GateManagerBehavior INSTANCE = new GateManagerBehavior();

    private GateManagerBehavior() { }

    public GateState<?> get(Stargate stargate) {
        GateState<?> oldState = stargate.stateOrNull(GateState.state);
        return oldState != null ? oldState : new GateState.Closed();
    }

    public void set(Stargate stargate, GateState<?> newState) {
        GateState<?> oldState = this.get(stargate);
        stargate.addState(newState);

        StargateGateStateEvents.notify(events ->
                events.stargate$gateState(stargate, oldState, newState));

        stargate.setChanged();
    }

    public static GateManagerBehavior get() {
        return INSTANCE;
    }
}