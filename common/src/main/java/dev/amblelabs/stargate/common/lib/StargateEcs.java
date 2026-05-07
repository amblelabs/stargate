package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEventsRegistry;
import dev.drtheo.ecs.state.TAbstractStateRegistry;

public class StargateEcs {

    public static final TAbstractStateRegistry States = new TAbstractStateRegistry() { };

    public static void init() {
        TAbstractStateRegistry.debug = IXplatAbstractions.INSTANCE.isDev();
    }

    public static void registerAll() {
        initState();
        States.freeze();

        initEvents();
        TEventsRegistry.freeze();

        initBehavior();
        TBehaviorRegistry.freeze();
    }

    public static void initState() {
        States.register(PrototypeIdentityState.state);
    }

    public static void initEvents() {
    }

    public static void initBehavior() {
    }
}
