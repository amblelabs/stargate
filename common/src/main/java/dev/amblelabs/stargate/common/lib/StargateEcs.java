package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.ecs.event.*;
import dev.amblelabs.stargate.common.impl.ecs.behavior.*;
import dev.amblelabs.stargate.common.impl.ecs.state.*;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEventsRegistry;
import dev.drtheo.ecs.state.TAbstractStateRegistry;

public class StargateEcs {

    public static final TAbstractStateRegistry States = new TAbstractStateRegistry() { };
    public static final TAbstractStateRegistry StaticStates = new TAbstractStateRegistry() { };

    public static void init() {
        TAbstractStateRegistry.debug = XplatAbstractions.INSTANCE.isDev();
    }

    public static void registerAll() {
        initState();

        States.freeze();
        StaticStates.freeze();
        TEventsRegistry.freeze();

        initBehavior();
        TBehaviorRegistry.freeze();
    }

    public static void initState() {
        States.register(PrototypeIdentityState.type);
        GateState.register(States);

        States.register(ShapeState.type);
        States.register(ChevronState.type);
        States.register(LevelState.type);

        States.register(C7State.type);
        States.register(IrisState.type);
    }

    public static void initBehavior() {
        TBehaviorRegistry.register(IrisBehavior::new);
        TBehaviorRegistry.register(PrototypeBehavior::new);
        TBehaviorRegistry.register(ShapeBehavior::new);
        TBehaviorRegistry.register(C7Behavior::new);

        GenericGateBehavior.registerAll();
        TBehaviorRegistry.register(GateManagerBehavior::get);
        TBehaviorRegistry.register(SpacialResistanceBehavior::new);
        TBehaviorRegistry.register(KawooshBehavior::new);
    }
}
