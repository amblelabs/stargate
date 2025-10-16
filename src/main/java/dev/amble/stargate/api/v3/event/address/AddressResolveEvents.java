package dev.amble.stargate.api.v3.event.address;

import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface AddressResolveEvents extends TEvents {

    Type<AddressResolveEvents> event = new Type<>(AddressResolveEvents.class);

    AddressResolveEvent.Result resolve(Stargate stargate, long targetAddress, int length);
}
