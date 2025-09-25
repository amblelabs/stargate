package dev.amble.stargate.api.v4.example.event.state;

import dev.amble.stargate.api.v4.data.StateResolveError;
import dev.amble.stargate.api.v4.data.TState;
import dev.amble.stargate.api.v4.event.TEvent;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.example.event.StargateEvents;

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
