package dev.amble.stargate.api.gates.event.tick;

import dev.amble.stargate.api.gates.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface StargateTickEvents extends TEvents {

    Type<StargateTickEvents> event = new Type<>(StargateTickEvents.class);

    default void tick(Stargate stargate) { }
}
