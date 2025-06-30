package dev.amble.stargate.api;

import dev.amble.stargate.api.v2.GateState;

public interface StargateAccessor {

    void setState(GateState gate);
}
