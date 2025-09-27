package dev.amble.stargate.init;

import dev.amble.stargate.api.v3.behavior.*;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.event.render.StargateRenderEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.ClientIrisState;
import dev.amble.stargate.api.v3.state.IrisState;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;
import dev.drtheo.yaar.state.TStateRegistry;

public class StargateYAARs {

    // TODO: move to a registry
    public static void init() {
        initEvents();
        TEventsRegistry.freeze();

        initBehavior();
        initState();
    }

    private static void initBehavior() {
        TBehaviorRegistry.register(GateManagerBehavior::new);

        TBehaviorRegistry.register(DestinyBehaviors.Closed::new);
        TBehaviorRegistry.register(DestinyBehaviors.Opening::new);
        TBehaviorRegistry.register(DestinyBehaviors.Open::new);

        TBehaviorRegistry.register(OrlinBehavior.Open::new);

        TBehaviorRegistry.register(IrisBehavior::new);
        TBehaviorRegistry.register(ClientIrisBehavior::new); // TODO: move to client
    }

    private static void initState() {
        TStateRegistry.register(ClientIrisState.state);
        TStateRegistry.register(IrisState.state);

        TStateRegistry.register(BasicGateStates.Closed.state);
        TStateRegistry.register(BasicGateStates.Opening.state);
        TStateRegistry.register(BasicGateStates.Open.state);
    }

    private static void initEvents() {
        TEventsRegistry.register(StargateEvents.event);
        TEventsRegistry.register(StargateEvents.State.event);
        TEventsRegistry.register(StargateBlockEvents.event);

        // TODO: move to client
        TEventsRegistry.register(StargateRenderEvents.event);
    }
}
