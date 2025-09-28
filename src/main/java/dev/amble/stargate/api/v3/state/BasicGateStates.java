package dev.amble.stargate.api.v3.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.network.StargateRef;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface BasicGateStates<T extends TState<T>> extends TState<T> {

    StateType gateState();

    class Closed implements BasicGateStates<Closed>, NbtSerializer {

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

    final class Opening implements BasicGateStates<Opening>, NbtSerializer {

        public static final Type<Opening> state = new NbtBacked<>(StargateMod.id("generic/opening")) {

            @Override
            public Opening fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
                String id = nbt.getString("address");
                boolean caller = nbt.getBoolean("caller");
                float kawooshHeight = nbt.getFloat("kawooshHeight");
                return new Opening(id, caller, kawooshHeight);
            }
        };

        public static final int TICKS_PER_KAWOOSH = 4 * 20;

        public final String address;
        public final boolean caller;

        public float kawooshHeight;
        public int timer;

        public Opening(String address, boolean caller) {
            this(address, caller, 0);
        }

        private Opening(String address, boolean caller, float kawooshHeight) {
            this.address = address;
            this.caller = caller;
            this.kawooshHeight = kawooshHeight;
        }

        @Override
        public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
            nbt.putString("address", address);
            nbt.putBoolean("caller", caller);
            nbt.putFloat("kawooshHeight", kawooshHeight);
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

    class Open implements BasicGateStates<Open>, NbtSerializer {

        public static final Type<Open> state = new NbtBacked<>(StargateMod.id("generic/open")) {
            @Override
            public Open fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
                UUID id = nbt.getUuid("address");
                boolean caller = nbt.getBoolean("caller");

                return new Open(new StargateRef(id, isClient), caller);
            }
        };

        public static final int TICKS_PER_OPEN = 30 * 20;
        public static final int TELEPORT_FREQUENCY = 10;
        public static final int TELEPORT_DELAY = 20;

        public final @NotNull StargateRef target;
        public final boolean caller;

        public int timer;

        public Open(@NotNull StargateRef target, boolean caller) {
            this.target = target;
            this.caller = caller;
        }

        @Override
        public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
            nbt.putUuid("address", this.target.id());
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
