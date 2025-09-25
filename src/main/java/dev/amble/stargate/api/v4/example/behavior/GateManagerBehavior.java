package dev.amble.stargate.api.v4.example.behavior;

import dev.amble.stargate.api.v4.behavior.TBehavior;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.example.event.StargateEvents;
import dev.amble.stargate.api.v4.example.event.state.StargateStateEvent;
import dev.amble.stargate.api.v4.example.state.GateState;
import dev.amble.stargate.api.v4.state.TState;

public class GateManagerBehavior implements TBehavior, StargateEvents {

    @Override
    public void onCreated(Stargate stargate) {
        GateState gateState = stargate.state(GateState.state);

        if (gateState.currentState() == null)
            this.set(stargate, gateState.createDefaultState());
    }

    public void set(Stargate stargate, TState<?> newState) {
        GateState gateState = stargate.state(GateState.state);
        TState<?> oldState = null;

        if (gateState.currentState() != null)
            oldState = stargate.removeState(gateState.currentState());

        gateState.currentState(newState.type());
        stargate.addState(newState);

        TEvents.handle(new StargateStateEvent(stargate, oldState, newState));
    }
}
