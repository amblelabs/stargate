package dev.amble.stargate.api.gates.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.data.StargateRef;
import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.state.address.GlobalAddressState;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface GateState<T extends TState<T> & GateState<T>> extends TState<T> {

    StateType gateState();

    class Closed implements GateState<Closed>, NbtSerializer {

        public static final Type<Closed> state = new NbtBacked<>(StargateMod.id("generic/closed")) {
            @Override
            public Closed fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
                return new Closed(nbt);
            }
        };

        public static final int TICKS_PER_CHEVRON = 20;

        public int locked;
        public boolean locking;

        public String address;
        public int timer;

        public Closed() {
            this.address = "";
        }

        private Closed(NbtCompound nbt) {
            this.locked = nbt.getInt("locked");
            this.locking = nbt.getBoolean("locking");
            this.address = nbt.getString("address");
        }

        @Override
        public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
            nbt.putInt("locked", locked);
            nbt.putBoolean("locking", locking);
            nbt.putString("address", address);
        }

        @Override
        public Type<Closed> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.CLOSED;
        }
    }

    final class Opening implements GateState<Opening>, NbtSerializer {

        public static final Type<Opening> state = new NbtBacked<>(StargateMod.id("generic/opening")) {

            @Override
            public Opening fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
                long address = nbt.getLong("address");
                boolean caller = nbt.getBoolean("caller");
                //float kawooshHeight = nbt.getFloat("kawooshHeight");

                return new Opening(StargateRef.resolveGlobal(address, isClient), caller, 0);
            }
        };

        public static final int TICKS_PER_KAWOOSH = 4 * 20;

        public final Stargate target;
        public final boolean caller;

        public float kawooshHeight;
        public int timer;

        public Opening(Stargate target, boolean caller) {
            this(target, caller, 0);
        }

        private Opening(Stargate target, boolean caller, float kawooshHeight) {
            this.target = target;
            this.caller = caller;
            this.kawooshHeight = kawooshHeight;
        }

        @Override
        public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
            nbt.putLong("address", target.resolve(GlobalAddressState.state).address());
            nbt.putBoolean("caller", caller);
            //nbt.putFloat("kawooshHeight", kawooshHeight);
        }

        @Override
        public Type<Opening> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.OPENING;
        }
    }

    class Open implements GateState<Open>, NbtSerializer {

        public static final Type<Open> state = new NbtBacked<>(StargateMod.id("generic/open")) {
            @Override
            public Open fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
                long address = nbt.getLong("address");
                boolean caller = nbt.getBoolean("caller");

                return new Open(StargateRef.resolveGlobal(address, isClient), caller);
            }
        };

        public static final int TICKS_PER_OPEN = 30 * 20;
        public static final int TELEPORT_FREQUENCY = 10;
        public static final int TELEPORT_DELAY = 20;

        public final Stargate target;
        public final boolean caller;

        public int timer;

        public Open(Stargate target, boolean caller) {
            this.target = target;
            this.caller = caller;
        }

        @Override
        public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
            nbt.putLong("address", target.resolve(GlobalAddressState.state).address());
            nbt.putBoolean("caller", this.caller);
        }

        @Override
        public Type<Open> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.OPEN;
        }
    }

    enum StateType {
        CLOSED,
        OPENING,
        OPEN
    }
}
