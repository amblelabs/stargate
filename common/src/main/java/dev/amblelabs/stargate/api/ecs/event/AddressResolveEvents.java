package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

public interface AddressResolveEvents extends TEvents {

    EventGroup<AddressResolveEvents> type = new EventGroup<>(AddressResolveEvents.class);

    AddressResolveEvent.Result resolve(Stargate stargate, String targetAddress, int length);
}