package dev.amble.stargate.api.v3.event;

import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateCreatedEvent(Stargate stargate) implements TEvent.Notify<StargateEvents> {
    @Override
    public void handle(StargateEvents handler) throws StateResolveError {
        handler.onCreated(stargate);
    }

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }
}
