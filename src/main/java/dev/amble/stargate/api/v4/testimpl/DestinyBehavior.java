package dev.amble.stargate.api.v4.testimpl;

import dev.amble.stargate.api.v4.GateBehavior;
import dev.amble.stargate.api.v4.GateKernel;
import dev.amble.stargate.api.v4.GateState;

public class DestinyBehavior {

    public static class Closed implements GateBehavior<DestinyState.Closed> {

        @Override
        public GateState.Type<DestinyState.Closed> type() {
            return DestinyState.CLOSED;
        }

        @Override
        public void tick(GateKernel kernel, DestinyState.Closed state) {
            System.out.println("Ticking closed destiny state");
        }
    }
}
