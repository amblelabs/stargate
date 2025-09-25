package dev.amble.stargate.api.v4.example;

import dev.amble.stargate.api.v4.behavior.TBehaviorRegistry;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.event.TEventsRegistry;
import dev.amble.stargate.api.v4.example.behavior.DestinyBehavior;
import dev.amble.stargate.api.v4.example.behavior.GateManagerBehavior;
import dev.amble.stargate.api.v4.example.event.StargateEvents;
import dev.amble.stargate.api.v4.example.event.StargateTickEvent;
import dev.amble.stargate.api.v4.example.state.DestinyState;
import dev.amble.stargate.api.v4.example.state.GateState;
import dev.amble.stargate.api.v4.state.TStateRegistry;

public class Main {
    public static void main(String[] args) {
        TEventsRegistry.register(StargateEvents.event);
        TEventsRegistry.register(StargateEvents.State.event);
        TEventsRegistry.freeze();

        TStateRegistry.debug = true;
        TStateRegistry.register(GateState.state);
        TStateRegistry.register(DestinyState.Closed.state);
        TStateRegistry.register(DestinyState.Opening.state);
        TStateRegistry.freeze();

        TBehaviorRegistry.register(GateManagerBehavior::new);
        TBehaviorRegistry.register(DestinyBehavior.Closed::new);
        TBehaviorRegistry.freeze();

        Stargate stargate = DestinyState.createGate(true);

        TEvents.handle(new StargateTickEvent(stargate));

        System.out.println(stargate.state(DestinyState.Opening.state));
    }
}
