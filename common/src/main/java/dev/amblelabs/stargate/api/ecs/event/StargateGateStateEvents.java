package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.drtheo.ecs.event.TEvents;

public interface StargateGateStateEvents extends TEvents {

    EventGroup<StargateGateStateEvents> type = new EventGroup<>(StargateGateStateEvents.class);

    void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState);
}