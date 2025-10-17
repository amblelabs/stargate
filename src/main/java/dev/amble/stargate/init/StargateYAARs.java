package dev.amble.stargate.init;

import dev.amble.stargate.api.behavior.*;
import dev.amble.stargate.api.behavior.address.AddressBehaviors;
import dev.amble.stargate.api.behavior.iris.IrisBehavior;
import dev.amble.stargate.api.event.address.AddressResolveEvents;
import dev.amble.stargate.api.event.address.StargateRemoveEvents;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.init.StargateUpdateEvents;
import dev.amble.stargate.api.event.state.StargateTStateEvents;
import dev.amble.stargate.api.event.tick.StargateTickEvents;
import dev.amble.stargate.api.event.block.StargateBlockEvents;
import dev.amble.stargate.api.event.state.gate.StargateGateStateEvents;
import dev.amble.stargate.api.event.tp.StargateTpEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.*;
import dev.amble.stargate.api.state.iris.IrisState;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import dev.drtheo.yaar.event.TEventsRegistry;
import dev.drtheo.yaar.state.TAbstractStateRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class StargateYAARs {

    public static final TAbstractStateRegistry States = new TAbstractStateRegistry() { };

    public static void init() {
        initEvents();
        if (shouldFreeze()) TEventsRegistry.freeze();

        initBehavior();
        if (shouldFreeze()) TBehaviorRegistry.freeze();

        initState();
        if (shouldFreeze()) States.freeze();
    }

    // this is needed so we don't freeze registries before client has registered its own stuff
    private static boolean shouldFreeze() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    private static void initBehavior() {
        TBehaviorRegistry.register(GateManagerBehavior::new);

        GenericGateBehaviors.registerAll();

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
        // base events
        TEventsRegistry.register(StargateCreatedEvents.event);
        TEventsRegistry.register(StargateLoadedEvents.event);
        TEventsRegistry.register(StargateUpdateEvents.event);
        TEventsRegistry.register(StargateTStateEvents.event);

        TEventsRegistry.register(StargateTickEvents.event);
        TEventsRegistry.register(StargateBlockEvents.event);

        // gate state
        TEventsRegistry.register(StargateGateStateEvents.event);
        TEventsRegistry.register(StargateTpEvents.event);

        // address shit
        TEventsRegistry.register(StargateRemoveEvents.event);
        TEventsRegistry.register(AddressResolveEvents.event);
    }
}
