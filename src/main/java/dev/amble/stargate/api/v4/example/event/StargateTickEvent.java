package dev.amble.stargate.api.v4.example.event;

import dev.amble.stargate.api.v4.state.StateResolveError;
import dev.amble.stargate.api.v4.event.TEvent;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.Stargate;

public record StargateTickEvent(Stargate stargate) implements TEvent.Notify<StargateEvents> {

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }

    @Override
    public void handle(StargateEvents handler) throws StateResolveError {
        handler.tick(stargate);
    }
}
