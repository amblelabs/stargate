package dev.amble.stargate.api.v3;

import net.minecraft.nbt.NbtCompound;

public abstract class BasicGateStates<Self extends BasicGateStates<Self>> implements GateStates<Self> {

    private final EnumMap<GateState.Type, StateDeserializer<Self>> map = new EnumMap<>(GateState.Type.VALUES, StateDeserializer[]::new);

    public BasicGateStates() {
        this.init(map::put);
    }

    protected abstract void init(StatePopulator<Self> populator);

    @Override
    public GateState<Self> fromNbt(GateState.Type type, NbtCompound nbt, boolean isClient) {
        return map.get(type).fromNbt(nbt, isClient);
    }

    @FunctionalInterface
    protected interface StatePopulator<S extends BasicGateStates<S>> {
        void put(GateState.Type type, StateDeserializer<S> behavior);
    }

    @FunctionalInterface
    protected interface StateDeserializer<S extends BasicGateStates<S>> {
        GateState<S> fromNbt(NbtCompound nbt, boolean isClient);
    }
}
