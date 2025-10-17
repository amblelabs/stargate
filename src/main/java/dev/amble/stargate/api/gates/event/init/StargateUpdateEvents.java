package dev.amble.stargate.api.gates.event.init;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.event.GenericStargateEvent;
import dev.drtheo.yaar.event.TEvents;

public interface StargateUpdateEvents extends TEvents {

    static void handleUpdate(Stargate stargate) {
        TEvents.handle(new GenericStargateEvent<>(event, stargate, StargateUpdateEvents::onUpdate));
    }

    Type<StargateUpdateEvents> event = new Type<>(StargateUpdateEvents.class);

    void onUpdate(Stargate stargate);
}
