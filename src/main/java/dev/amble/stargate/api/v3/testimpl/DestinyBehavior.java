package dev.amble.stargate.api.v3.testimpl;

import dev.amble.stargate.api.v3.GateBehavior;
import dev.amble.stargate.api.v3.GateKernel;
import dev.amble.stargate.api.v3.GateState;

public class DestinyBehavior {

    public static class Closed implements GateBehavior<DestinyState.Closed> {

        @Override
        public GateState.Type<DestinyState.Closed> type() {
            return DestinyState.CLOSED;
        }

        boolean shit;

        @Override
        public void tick(GateKernel kernel, DestinyState.Closed state) {
            shit = !shit;
        }
    }
}
