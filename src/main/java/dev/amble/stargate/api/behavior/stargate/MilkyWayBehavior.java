package dev.amble.stargate.api.behavior.stargate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.state.stargate.MilkyWayState;
import dev.drtheo.yaar.behavior.TBehavior;

public class MilkyWayBehavior implements TBehavior, StargateCreatedEvents {

    @Override
    public void onCreated(Stargate stargate) {
        stargate.addState(new MilkyWayState());
    }
}
