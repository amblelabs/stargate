package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.ecs.event.*;
import dev.amblelabs.stargate.common.impl.ecs.behavior.*;
import dev.amblelabs.stargate.common.impl.ecs.state.*;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEventsRegistry;
import dev.drtheo.ecs.state.TAbstractStateRegistry;

public class StargateEcs {

    public static final TAbstractStateRegistry States = new TAbstractStateRegistry() { };
    public static final TAbstractStateRegistry StaticStates = new TAbstractStateRegistry() { };

    public static void init() {
        TAbstractStateRegistry.debug = IXplatAbstractions.INSTANCE.isDev();
    }

    public static void registerAll() {
        initState();
        States.freeze();
        StaticStates.freeze();

        initEvents();
        TEventsRegistry.freeze();

        initBehavior();
        TBehaviorRegistry.freeze();
    }

    public static void initState() {
        States.register(PrototypeIdentityState.state);
        GateState.register(States);

        States.register(ShapeState.state);
        States.register(ChevronState.state);
        States.register(LevelState.state);

        States.register(C7State.state);
        States.register(IrisState.state);
    }

    public static void initEvents() {
        TEventsRegistry.register(StargateLifecycleEvents.type);
        TEventsRegistry.register(StargateBlockEvents.type);
        TEventsRegistry.register(IrisEvents.type);
        TEventsRegistry.register(DHDBlockEvents.type);

        TEventsRegistry.register(AddressResolveEvents.type);
        TEventsRegistry.register(StargateGateStateEvents.type);
        TEventsRegistry.register(StargateTickEvents.type);
        TEventsRegistry.register(StargateTpEvents.type);
    }

    public static void initBehavior() {
        TBehaviorRegistry.register(IrisBehavior::new);
        TBehaviorRegistry.register(PrototypeBehavior::new);
        TBehaviorRegistry.register(ShapeBehavior::new);
        TBehaviorRegistry.register(C7Behavior::new);

        GenericGateBehavior.registerAll();
        TBehaviorRegistry.register(GateManagerBehavior::new);
    }
}
