package dev.amble.stargate.api.v4;

import dev.amble.stargate.api.v4.testimpl.DestinyBehavior;
import dev.amble.stargate.api.v4.testimpl.DestinyState;

public class Main {
    public static void main(String[] args) {
        BehaviorRegistry.register(new DestinyBehavior.Closed());
        StateRegistry.register(DestinyState.GROUP);

        GateKernel kernel = new GateKernel();
        kernel.internal$addState(new DestinyState.Closed());


        kernel.tick();
    }
}
