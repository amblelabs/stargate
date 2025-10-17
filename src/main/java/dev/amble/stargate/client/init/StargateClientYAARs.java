package dev.amble.stargate.client.init;

import dev.amble.stargate.api.gates.behavior.client.ClientGenericGateBehavior;
import dev.amble.stargate.api.gates.behavior.client.ClientIrisBehavior;
import dev.amble.stargate.api.gates.behavior.client.ClientMilkyWayBehavior;
import dev.amble.stargate.api.gates.behavior.client.ClientOrlinBehavior;
import dev.amble.stargate.api.gates.event.render.StargateRenderEvents;
import dev.amble.stargate.api.gates.state.iris.ClientIrisState;
import dev.amble.stargate.api.gates.state.stargate.client.*;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;

import static dev.amble.stargate.init.StargateYAARs.States;

public class StargateClientYAARs {

    public static void init() {
        // events
        TEventsRegistry.register(StargateRenderEvents.event);
        TEventsRegistry.freeze();

        // stargate behaviors
        TBehaviorRegistry.register(ClientGenericGateBehavior::new);
        TBehaviorRegistry.register(ClientMilkyWayBehavior::new);
        TBehaviorRegistry.register(ClientOrlinBehavior::new);

        TBehaviorRegistry.register(ClientIrisBehavior::new);
        TBehaviorRegistry.freeze();

        // stargate states
        States.register(ClientGenericGateState.state);
        States.register(ClientMilkyWayState.state);
        States.register(ClientOrlinState.state);
        States.register(ClientPegasusState.state);
        States.register(ClientDestinyState.state);

        States.register(ClientIrisState.state);
        States.freeze();
    }
}
