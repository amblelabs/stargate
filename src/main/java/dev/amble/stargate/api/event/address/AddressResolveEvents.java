package dev.amble.stargate.api.event.address;

import dev.amble.stargate.api.ServerStargate;
import dev.drtheo.yaar.event.TEvents;

public interface AddressResolveEvents extends TEvents {

    Type<AddressResolveEvents> event = new Type<>(AddressResolveEvents.class);

    AddressResolveEvent.Result resolve(ServerStargate stargate, long targetAddress, int length);
}
