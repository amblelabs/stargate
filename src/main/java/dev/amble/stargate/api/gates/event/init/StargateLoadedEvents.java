package dev.amble.stargate.api.gates.event.init;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.event.GenericStargateEvent;
import dev.drtheo.yaar.event.TEvents;

public interface StargateLoadedEvents extends TEvents {

    static void handleLoad(Stargate stargate) {
        TEvents.handle(new GenericStargateEvent<>(event, stargate, StargateLoadedEvents::onLoaded));
    }

    Type<StargateLoadedEvents> event = new Type<>(StargateLoadedEvents.class);

    void onLoaded(Stargate stargate);
}
