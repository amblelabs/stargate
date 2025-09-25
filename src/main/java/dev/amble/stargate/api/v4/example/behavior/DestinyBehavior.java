package dev.amble.stargate.api.v4.example.behavior;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v4.behavior.Resolve;
import dev.amble.stargate.api.v4.behavior.TBehavior;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.example.event.StargateEvents;
import dev.amble.stargate.api.v4.example.state.DestinyState;
import dev.amble.stargate.api.v4.state.TState;

public interface DestinyBehavior {

    class Closed implements TBehavior, StargateEvents {

        @Resolve
        private final GateManagerBehavior manager = handler();

        @Override
        public void onStateChanged(Stargate stargate, TState<?> oldState, TState<?> newState) {
            if (newState instanceof DestinyState.Closed)
                StargateMod.LOGGER.info("Closed state added!");
        }

        @Override
        public void tick(Stargate stargate) {
            DestinyState.Closed state = stargate.state(DestinyState.Closed.state);
            StargateMod.LOGGER.info("ticking closed state!");

            manager.set(stargate, new DestinyState.Opening());
        }
    }
}
