package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

// TODO: use EventSingle
public interface StargateTickEvents extends TEvents {
    EventGroup<StargateTickEvents> type = new EventGroup<>(StargateTickEvents.class);

    void tick(Stargate someGate);
}
