package dev.amble.stargate.api.v3.impl;

import dev.amble.stargate.api.v3.GateBehavior;
import dev.amble.stargate.api.v3.GateBehaviors;
import dev.amble.stargate.api.v3.GateState;

public class DestinyBehaviours implements GateBehaviors<DestinyStates> {
    @Override
    public <T extends GateState<T, DestinyStates>> GateBehavior<DestinyStates, T> fromState(T state) {
        return state.;
    }
}
