package dev.amble.stargate.api.behavior;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.state.gate.StargateGateStateEvent;
import dev.amble.stargate.api.state.GateState;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;

public class GateManagerBehavior implements TBehavior {

    public static GateManagerBehavior INSTANCE = null;

    public GateManagerBehavior() {
        INSTANCE = this;
    }

    public void set(Stargate stargate, GateState<?> newState) {
        TState.Type<?> oldStateType = stargate.getGateStateType();
        GateState<?> oldState = null;

        if (oldStateType != null)
            oldState = (GateState<?>) stargate.removeState(oldStateType);

        stargate.setGateState(newState);
        stargate.markDirty();

        TEvents.handle(new StargateGateStateEvent(stargate, oldState, newState));
    }
}
