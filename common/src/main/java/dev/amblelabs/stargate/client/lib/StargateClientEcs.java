package dev.amblelabs.stargate.client.lib;

import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEventsRegistry;

import static dev.amblelabs.stargate.common.lib.StargateEcs.States;

public class StargateClientEcs {

    public static void registerAll() {
        StargateEcs.initState();
        initState();
        States.freeze();

        StargateEcs.initEvents();
        initEvents();
        TEventsRegistry.freeze();

        StargateEcs.initBehavior();
        initBehavior();
        TBehaviorRegistry.freeze();
    }

    public static void initState() {
        States.register(GeckoState.state);
    }

    public static void initEvents() {
    }

    public static void initBehavior() {
    }
}
