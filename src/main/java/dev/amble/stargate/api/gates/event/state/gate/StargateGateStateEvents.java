package dev.amble.stargate.api.gates.event.state.gate;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.state.GateState;
import dev.drtheo.yaar.event.TEvents;

public interface StargateGateStateEvents extends TEvents {

    Type<StargateGateStateEvents> event = new Type<>(StargateGateStateEvents.class);

    void onStateChanged(Stargate stargate, GateState<?> oldState, GateState<?> newState);
}
