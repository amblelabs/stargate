package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.drtheo.ecs.event.TEvents;

import java.util.function.Consumer;

public interface StargateGateStateEvents extends TEvents {

    Type<StargateGateStateEvents> type = new Type<>(StargateGateStateEvents.class);

    void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState);

    static void notify(Consumer<StargateGateStateEvents> handler) {
        TEvents.notify(type, handler);
    }
}