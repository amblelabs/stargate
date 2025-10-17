package dev.amble.stargate.api.event.tick;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface StargateTickEvents extends TEvents {

    Type<StargateTickEvents> event = new Type<>(StargateTickEvents.class);

    default void tick(Stargate stargate) { }
}
