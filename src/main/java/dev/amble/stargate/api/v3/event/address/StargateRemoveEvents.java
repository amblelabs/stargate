package dev.amble.stargate.api.v3.event.address;

import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface StargateRemoveEvents extends TEvents {

    Type<StargateRemoveEvents> event = new Type<>(StargateRemoveEvents.class);

    void remove(StargateServerData data, Stargate stargate);
}
