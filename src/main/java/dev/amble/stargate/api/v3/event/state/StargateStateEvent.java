package dev.amble.stargate.api.v3.event.state;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import dev.drtheo.yaar.state.TState;

public record StargateStateEvent(Stargate stargate, TState<?> oldState, TState<?> newState) implements TEvent.Notify<StargateEvents> {
    @Override
    public void handle(StargateEvents handler) throws StateResolveError {
        handler.onStateChanged(stargate, oldState, newState);
    }

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }
}
