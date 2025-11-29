package dev.amble.stargate.api.event.init;

import dev.amble.stargate.api.ServerStargate;
import dev.amble.stargate.api.event.GenericStargateEvent;
import dev.drtheo.yaar.event.TEvents;

public interface StargateCreatedEvents extends TEvents {

    static void handleCreation(ServerStargate stargate) {
        TEvents.handle(new GenericStargateEvent<>(event, stargate, StargateCreatedEvents::onCreated));
    }

    Type<StargateCreatedEvents> event = new Type<>(StargateCreatedEvents.class);

    void onCreated(ServerStargate stargate);
}
