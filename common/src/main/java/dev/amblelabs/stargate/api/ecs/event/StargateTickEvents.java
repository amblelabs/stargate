package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

public interface StargateTickEvents extends TEvents {
    Type<StargateTickEvents> type = new Type<>(StargateTickEvents.class);

    void tick(Stargate someGate);
}
