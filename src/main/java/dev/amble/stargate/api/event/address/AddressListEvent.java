package dev.amble.stargate.api.event.address;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;

import java.util.function.LongConsumer;

public record AddressListEvent(Stargate stargate, LongConsumer consumer) implements TEvent.Notify<AddressIdsListEvents> {

    @Override
    public TEvents.BaseType<AddressIdsListEvents> type() {
        return AddressIdsListEvents.event;
    }

    @Override
    public void handle(AddressIdsListEvents events) {
        consumer.accept(events.findAddressId(stargate));
    }
}
