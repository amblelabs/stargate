package dev.amble.stargate.init;

import dev.amble.stargate.api.v3.behavior.*;
import dev.amble.stargate.api.v3.behavior.client.ClientGenericGateBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientIrisBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientMilkyWayBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientOrlinBehavior;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.event.render.StargateRenderEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.IrisState;
import dev.amble.stargate.api.v3.state.client.*;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;
import dev.drtheo.yaar.state.TStateRegistry;

public class StargateYAARs {

    // TODO: move to a registry
    public static void init() {
        initEvents();
        TEventsRegistry.freeze();

        initBehavior();
        TBehaviorRegistry.freeze();

        initState();
        TStateRegistry.freeze();
    }

    private static void initBehavior() {
        TBehaviorRegistry.register(GateManagerBehavior::new);

        TBehaviorRegistry.register(DestinyBehaviors.Closed::new);
        TBehaviorRegistry.register(DestinyBehaviors.Opening::new);
        TBehaviorRegistry.register(DestinyBehaviors.Open::new);

        TBehaviorRegistry.register(OrlinBehavior.Open::new);

        TBehaviorRegistry.register(ClientGenericGateBehavior::new);
        TBehaviorRegistry.register(ClientMilkyWayBehavior::new);
        TBehaviorRegistry.register(ClientOrlinBehavior::new);

        TBehaviorRegistry.register(SpacialResistanceBehavior::new);
        TBehaviorRegistry.register(StargateTpEffectsBehavior::new);

        TBehaviorRegistry.register(IrisBehavior::new);
        TBehaviorRegistry.register(ClientIrisBehavior::new); // TODO: move to client
    }

    private static void initState() {
        TStateRegistry.register(BasicGateStates.Closed.state);
        TStateRegistry.register(BasicGateStates.Opening.state);
        TStateRegistry.register(BasicGateStates.Open.state);

        TStateRegistry.register(ClientGenericGateState.state);
        TStateRegistry.register(ClientMilkyWayState.state);
        TStateRegistry.register(ClientOrlinState.state);
        TStateRegistry.register(ClientPegasusState.state);
        TStateRegistry.register(ClientDestinyState.state);

        TStateRegistry.register(ClientIrisState.state);
        TStateRegistry.register(IrisState.state);
    }

    private static void initEvents() {
        TEventsRegistry.register(StargateEvents.event);
        TEventsRegistry.register(StargateEvents.State.event);
        TEventsRegistry.register(StargateBlockEvents.event);

        // TODO: move to client
        TEventsRegistry.register(StargateRenderEvents.event);
    }
}
