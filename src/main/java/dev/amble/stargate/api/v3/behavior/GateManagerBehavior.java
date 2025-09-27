package dev.amble.stargate.api.v3.behavior;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.state.StargateStateEvent;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;

public class GateManagerBehavior implements TBehavior, StargateEvents {

    public static GateManagerBehavior INSTANCE = null;

    public GateManagerBehavior() {
        INSTANCE = this;
    }

    public void set(Stargate stargate, TState<?> newState) {
        TState.Type<?> oldStateType = stargate.currentStateType();
        TState<?> oldState = null;

        if (oldStateType != null)
            oldState = stargate.removeState(oldStateType);

        stargate.setCurState(newState.type());
        stargate.addState(newState);

        stargate.markDirty();

        TEvents.handle(new StargateStateEvent(stargate, oldState, newState));
    }
}
