package dev.amble.stargate.api.v3.event.state;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import dev.drtheo.yaar.state.TState;

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
