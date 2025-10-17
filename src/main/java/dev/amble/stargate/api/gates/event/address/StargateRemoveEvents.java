package dev.amble.stargate.api.gates.event.address;

import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.gates.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface StargateRemoveEvents extends TEvents {

    Type<StargateRemoveEvents> event = new Type<>(StargateRemoveEvents.class);

    void remove(StargateServerData data, Stargate stargate);
}
