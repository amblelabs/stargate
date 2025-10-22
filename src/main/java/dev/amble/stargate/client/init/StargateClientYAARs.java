package dev.amble.stargate.client.init;

import dev.amble.stargate.api.behavior.client.ClientDestinyBehavior;
import dev.amble.stargate.api.behavior.client.ClientMilkyWayBehavior;
import dev.amble.stargate.api.behavior.client.ClientOrlinBehavior;
import dev.amble.stargate.api.behavior.client.ClientPegasusBehavior;
import dev.amble.stargate.api.behavior.iris.ClientIrisBehavior;
import dev.amble.stargate.api.event.render.StargateRenderEvents;
import dev.amble.stargate.api.state.iris.ClientIrisState;
import dev.amble.stargate.api.state.stargate.client.*;
import dev.amble.stargate.init.StargateYAARs;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;

import static dev.amble.stargate.init.StargateYAARs.States;

public class StargateClientYAARs {

    public static void init() {
        // events
        StargateYAARs.initEvents();
        TEventsRegistry.register(StargateRenderEvents.event);
        TEventsRegistry.freeze();

        // stargate behaviors
        StargateYAARs.initBehavior();
        TBehaviorRegistry.register(ClientMilkyWayBehavior::new);
        TBehaviorRegistry.register(ClientPegasusBehavior::new);
        TBehaviorRegistry.register(ClientDestinyBehavior::new);
        TBehaviorRegistry.register(ClientOrlinBehavior::new);

        TBehaviorRegistry.register(ClientIrisBehavior::new);
        TBehaviorRegistry.freeze();

        // stargate states
        StargateYAARs.initState();
        States.register(ClientAbstractStargateState.state);
        States.register(ClientMilkyWayState.state);
        States.register(ClientOrlinState.state);
        States.register(ClientPegasusState.state);
        States.register(ClientDestinyState.state);

        States.register(ClientIrisState.state);
        States.freeze();
    }
}
