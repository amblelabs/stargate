package dev.amble.stargate.api.v4.testimpl;

import dev.amble.stargate.api.v4.GateState;
import net.minecraft.nbt.NbtCompound;

public class DestinyState {

    public static final GateState.Group GROUP = GateState.Group.create();

    public static final GateState.Type<Closed> CLOSED = GateState.StargateChildType.closed(GROUP, Closed::fromNbt);

    public static class Closed implements GateState<Closed> {

        @Override
        public Type<Closed> type() {
            return CLOSED;
        }

        @Override
        public void toNbt(NbtCompound nbt) {
        }

        public void loadNbt(NbtCompound nbt, boolean isClosed) {

        }

        public static Closed fromNbt(NbtCompound nbt, boolean isClosed) {
            Closed closed = new Closed();
            closed.loadNbt(nbt, isClosed);

            return closed;
        }
    }
}
