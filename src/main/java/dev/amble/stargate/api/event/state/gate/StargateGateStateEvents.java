package dev.amble.stargate.api.event.state.gate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.drtheo.yaar.event.TEvents;

public interface StargateGateStateEvents extends TEvents {

    Type<StargateGateStateEvents> event = new Type<>(StargateGateStateEvents.class);

    void onStateChanged(Stargate stargate, GateState<?> oldState, GateState<?> newState);
}
