package dev.amble.stargate.api.event.address;

import dev.amble.stargate.api.ServerStargate;
import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface StargateRemoveEvents extends TEvents {

    Type<StargateRemoveEvents> event = new Type<>(StargateRemoveEvents.class);

    void remove(StargateServerData data, ServerStargate stargate);
}
