package dev.amble.stargate.api.event.address;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvents;

public interface AddressIdsListEvents extends TEvents {

    Type<AddressIdsListEvents> event = new Type<>(AddressIdsListEvents.class);

    long findAddressId(Stargate stargate);
}
