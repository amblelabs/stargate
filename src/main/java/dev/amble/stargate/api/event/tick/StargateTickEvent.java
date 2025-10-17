package dev.amble.stargate.api.event.tick;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateTickEvent(Stargate stargate) implements TEvent.Notify<StargateTickEvents> {

    @Override
    public TEvents.BaseType<StargateTickEvents> type() {
        return StargateTickEvents.event;
    }

    @Override
    public void handle(StargateTickEvents handler) throws StateResolveError {
        handler.tick(stargate);
    }
}
