package dev.amblelabs.stargate.client.lib;

import dev.amblelabs.stargate.client.impl.ecs.behavior.ClientPuddleBehavior;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.impl.ecs.state.GlyphsState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEventsRegistry;

import static dev.amblelabs.stargate.common.lib.StargateEcs.States;
import static dev.amblelabs.stargate.common.lib.StargateEcs.StaticStates;

public class StargateClientEcs {

    public static void registerAll() {
        StargateEcs.initState();
        initState();
        States.freeze();
        StaticStates.freeze();

        StargateEcs.initEvents();
        initEvents();
        TEventsRegistry.freeze();

        StargateEcs.initBehavior();
        initBehavior();
        TBehaviorRegistry.freeze();
    }

    public static void initState() {
        StaticStates.register(GeckoState.state);
        StaticStates.add(GeckoState.Default.state);

        StaticStates.register(GlyphsState.state);
    }

    @SuppressWarnings("EmptyMethod")
    public static void initEvents() {
    }

    @SuppressWarnings("EmptyMethod")
    public static void initBehavior() {
        TBehaviorRegistry.register(ClientPuddleBehavior::new);
    }
}
