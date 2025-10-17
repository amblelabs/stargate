package dev.amble.stargate.api.event.state.gate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateGateStateEvent(Stargate stargate, GateState<?> oldState, GateState<?> newState) implements TEvent.Notify<StargateGateStateEvents> {

    @Override
    public void handle(StargateGateStateEvents handler) throws StateResolveError {
        handler.onStateChanged(stargate, oldState, newState);
    }

    @Override
    public TEvents.BaseType<StargateGateStateEvents> type() {
        return StargateGateStateEvents.event;
    }
}
