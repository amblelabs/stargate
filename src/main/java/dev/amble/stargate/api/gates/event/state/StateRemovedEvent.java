package dev.amble.stargate.api.gates.event.state;

import dev.amble.stargate.api.gates.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import dev.drtheo.yaar.state.TState;

public record StateRemovedEvent(Stargate stargate, TState<?> state) implements TEvent.Notify<StargateTStateEvents> {

    @Override
    public TEvents.BaseType<StargateTStateEvents> type() {
        return StargateTStateEvents.event;
    }

    @Override
    public void handle(StargateTStateEvents handler) throws StateResolveError {
        handler.onStateRemoved(stargate, state);
    }
}
