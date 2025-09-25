package dev.amble.stargate.api.v4.example.event.state;

import dev.amble.stargate.api.v4.data.StateResolveError;
import dev.amble.stargate.api.v4.data.TState;
import dev.amble.stargate.api.v4.event.TEvent;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.example.event.StargateEvents;

public record StateAddedEvent(Stargate stargate, TState<?> state) implements TEvent.Notify<StargateEvents.State> {
    @Override
    public TEvents.BaseType<StargateEvents.State> type() {
        return StargateEvents.State.event;
    }

    @Override
    public void handle(StargateEvents.State handler) throws StateResolveError {
        handler.onStateAdded(stargate, state);
    }
}
