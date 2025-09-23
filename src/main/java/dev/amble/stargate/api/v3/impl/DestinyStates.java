package dev.amble.stargate.api.v3.impl;

import dev.amble.stargate.api.v3.GateState;
import dev.amble.stargate.api.v3.GateStates;
import net.minecraft.nbt.NbtCompound;

public class DestinyStates implements GateStates<DestinyStates> {

    @Override
    public GateState<DestinyStates> getDefault() {
        return null;
    }

    @Override
    public GateState<DestinyStates> fromNbt(GateState.Type type, NbtCompound nbt, boolean isClient) {
        return null;
    }

    static class Closed extends GateStates.Closed<Closed, DestinyStates> {

        static final StargateChildType<Closed> TYPE = StargateChildType.closed(Closed::fromNbt);

        private String addressBuilder;

        @Override
        public Type<Closed> type() {
            return TYPE;
        }

        @Override
        public NbtCompound toNbt(NbtCompound nbt) {
            return null;
        }

        public static Closed fromNbt(NbtCompound nbt, boolean isClient) {
            Closed closed = new Closed();

            closed.loadNbt(nbt);
            return closed;
        }
    }


}
