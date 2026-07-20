package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateGateStateEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.drtheo.ecs.behavior.TBehavior;

import java.util.Objects;

public class GateManagerBehavior implements TBehavior {

    public void set(Stargate stargate, GateState<?> newState) {
        GateState<?> holder = stargate.stateOrNull(GateState.state);
        if (holder == null) stargate.addState(holder = new GateState.Closed());

        GateState<?> oldState = stargate.removeState(holder.type());
        stargate.addState(newState);

        StargateGateStateEvents.notify(events ->
                events.stargate$gateState(stargate, Objects.requireNonNull(oldState), newState));
    }
}