package dev.amble.stargate.api.behavior.stargate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.state.stargate.OrlinState;
import dev.drtheo.yaar.behavior.TBehavior;

public class OrlinBehavior implements TBehavior, StargateCreatedEvents, StargateLoadedEvents {

    @Override
    public void onCreated(Stargate stargate) {
        stargate.addState(new OrlinState());
    }

    @Override
    public void onLoaded(Stargate stargate) {
        stargate.addState(new OrlinState());
    }
}
