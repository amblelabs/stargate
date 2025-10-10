package dev.amble.stargate.init;

import dev.amble.stargate.api.v3.behavior.*;
import dev.amble.stargate.api.v3.behavior.address.AddressBehaviors;
import dev.amble.stargate.api.v3.behavior.client.ClientGenericGateBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientIrisBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientMilkyWayBehavior;
import dev.amble.stargate.api.v3.behavior.client.ClientOrlinBehavior;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.event.render.StargateRenderEvents;
import dev.amble.stargate.api.v3.state.GateState;
import dev.amble.stargate.api.v3.state.stargate.GateIdentityState;
import dev.amble.stargate.api.v3.state.iris.ClientIrisState;
import dev.amble.stargate.api.v3.state.iris.IrisState;
import dev.amble.stargate.api.v3.state.stargate.client.*;
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

        TBehaviorRegistry.register(BasicGateBehaviors.Closed::new);
        TBehaviorRegistry.register(BasicGateBehaviors.Opening::new);
        TBehaviorRegistry.register(BasicGateBehaviors.Open::new);

        TBehaviorRegistry.register(ClientGenericGateBehavior::new);
        TBehaviorRegistry.register(ClientMilkyWayBehavior::new);
        TBehaviorRegistry.register(ClientOrlinBehavior::new);

        TBehaviorRegistry.register(SpacialResistanceBehavior::new);
        TBehaviorRegistry.register(StargateTpEffectsBehavior::new);

        TBehaviorRegistry.register(IrisBehavior::new);
        TBehaviorRegistry.register(ClientIrisBehavior::new); // TODO: move to client

        AddressBehaviors.registerAll();
    }

    private static void initState() {
        TStateRegistry.register(GateState.Closed.state);
        TStateRegistry.register(GateState.Opening.state);
        TStateRegistry.register(GateState.Open.state);
        TStateRegistry.register(GateIdentityState.state);

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
