package dev.amble.stargate.init;

import dev.amble.stargate.api.gates.behavior.*;
import dev.amble.stargate.api.gates.behavior.address.AddressBehaviors;
import dev.amble.stargate.api.gates.event.state.StargateTStateEvents;
import dev.amble.stargate.api.gates.event.tick.StargateTickEvents;
import dev.amble.stargate.api.gates.event.block.StargateBlockEvents;
import dev.amble.stargate.api.gates.event.state.gate.StargateGateStateEvents;
import dev.amble.stargate.api.gates.event.tp.StargateTpEvents;
import dev.amble.stargate.api.gates.state.GateState;
import dev.amble.stargate.api.gates.state.stargate.GateIdentityState;
import dev.amble.stargate.api.gates.state.iris.IrisState;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;
import dev.drtheo.yaar.state.TStateRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class StargateYAARs {

    public static final TStateRegistry States = new TStateRegistry();

    public static void init() {
        initEvents();
        if (shouldFreeze()) TEventsRegistry.freeze();

        initBehavior();
        if (shouldFreeze()) TBehaviorRegistry.freeze();

        initState();
        if (shouldFreeze()) States.freeze();
    }

    private static boolean shouldFreeze() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    private static void initBehavior() {
        TBehaviorRegistry.register(GateManagerBehavior::new);

        TBehaviorRegistry.register(BasicGateBehaviors.Closed::new);
        TBehaviorRegistry.register(BasicGateBehaviors.Opening::new);
        TBehaviorRegistry.register(BasicGateBehaviors.Open::new);

        TBehaviorRegistry.register(SpacialResistanceBehavior::new);
        TBehaviorRegistry.register(StargateTpEffectsBehavior::new);

        TBehaviorRegistry.register(IrisBehavior::new);

        AddressBehaviors.registerAll();
    }

    private static void initState() {
        States.register(GateState.Closed.state);
        States.register(GateState.Opening.state);
        States.register(GateState.Open.state);
        States.register(GateIdentityState.state);

        States.register(IrisState.state);
    }

    private static void initEvents() {
        TEventsRegistry.register(StargateTStateEvents.event);
        TEventsRegistry.register(StargateBlockEvents.event);
        TEventsRegistry.register(StargateGateStateEvents.event);
        TEventsRegistry.register(StargateTpEvents.event);
        TEventsRegistry.register(StargateTickEvents.event);
    }
}
