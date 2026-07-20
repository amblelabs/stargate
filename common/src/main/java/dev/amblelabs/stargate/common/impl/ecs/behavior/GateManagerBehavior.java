package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateGateStateEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.drtheo.ecs.behavior.TBehavior;

import java.util.Objects;

public class GateManagerBehavior implements TBehavior {

    public void set(Stargate stargate, GateState<?> newState) {
        GateState.Holder holder = stargate.stateOrNull(GateState.Holder.state);

        if (holder == null) {
            holder = GateState.Holder.forStargate(stargate);
            stargate.addState(holder);
        }

        GateState<?> oldState = (GateState<?>) stargate.removeState(holder.current);

        stargate.addState(newState);
        holder.current = newState.type();

        StargateGateStateEvents.notify(events -> events.stargate$gateState(stargate,
                Objects.requireNonNull(oldState), newState));

        stargate.setChanged();
    }
}