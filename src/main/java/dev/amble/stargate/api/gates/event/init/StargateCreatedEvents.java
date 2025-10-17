package dev.amble.stargate.api.gates.event.init;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.event.GenericStargateEvent;
import dev.drtheo.yaar.event.TEvents;

public interface StargateCreatedEvents extends TEvents {

    static void handleCreation(Stargate stargate) {
        TEvents.handle(new GenericStargateEvent<>(event, stargate, StargateCreatedEvents::onCreated));
    }

    Type<StargateCreatedEvents> event = new Type<>(StargateCreatedEvents.class);

    void onCreated(Stargate stargate);
}
