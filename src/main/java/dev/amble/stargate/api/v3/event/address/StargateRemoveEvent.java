package dev.amble.stargate.api.v3.event.address;

import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateRemoveEvent(StargateServerData data, Stargate stargate) implements TEvent.Notify<StargateRemoveEvents> {

    @Override
    public void handle(StargateRemoveEvents handler) throws StateResolveError {
        handler.remove(data, stargate);
    }

    @Override
    public TEvents.BaseType<StargateRemoveEvents> type() {
        return StargateRemoveEvents.event;
    }
}
