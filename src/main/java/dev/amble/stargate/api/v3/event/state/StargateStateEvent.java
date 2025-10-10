package dev.amble.stargate.api.v3.event.state;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.state.GateState;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateStateEvent(Stargate stargate, GateState<?> oldState, GateState<?> newState) implements TEvent.Notify<StargateEvents> {
    @Override
    public void handle(StargateEvents handler) throws StateResolveError {
        handler.onStateChanged(stargate, oldState, newState);
    }

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }
}
