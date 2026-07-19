package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.NbtUtil;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GateState<T extends NbtState<T> & GateState<T>> extends NbtState<T> {

    StateType gateState();

    class Closed implements GateState<Closed> {

        public static final Type<Closed> state = new Type<>(StargateAPI.modLoc("generic/closed"), 0) {
            @Override
            public Closed fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                return new Closed();
            }
        };

        public static final int TICKS_PER_GLYPH = 5;

        public static final int TICKS_PER_GLYPH2 = 60;

        public int locked;
        public boolean locking;

        // FIXME: use a fixed size char array here instead
        public String address;
        public int timer;

        public Closed() {
            this.address = "";
        }

        private Closed(CompoundTag nbt) {
            this.locked = nbt.getInt("locked");
            this.locking = nbt.getBoolean("locking");
            this.address = nbt.getString("address");
        }

        // FIXME(perf)
        public boolean address$contains(char c) {
            return address.indexOf(c) != -1;
        }

        public int glyphIdxAtChevron(int chevronIdx) {
            return Glyph.charToIdx(address.charAt(chevronIdx));
        }

        @Override
        public Type<Closed> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.CLOSED;
        }

        @Override
        public void toNbt(CompoundTag nbt, Context context) {
            nbt.putInt("locked", locked);
            nbt.putBoolean("locking", locking);
            nbt.putString("address", address);
        }
    }

    final class Opening implements GateState<Opening> {

        public static final Type<Opening> state = new Type<>(StargateAPI.modLoc("generic/opening"), 0) {
            @Override
            public Opening fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                long address = nbt.getLong("address");
                boolean caller = nbt.getBoolean("caller");
                int timer = nbt.getInt("timer");
                float kawooshHeight = nbt.getFloat("kawooshHeight");

                return new Opening(Stargate.resolveGlobal(address, context),
                        caller, timer, kawooshHeight);
            }
        };

        public static final int TICKS_PER_KAWOOSH = 4 * 20;

        public final @Nullable Stargate target;
        public final boolean caller;

        public int timer;
        public float kawooshHeight;

        public Opening(Stargate target, boolean caller) {
            this(target, caller, 0, 0);
        }

        private Opening(@Nullable Stargate target, boolean caller, int timer, float kawooshHeight) {
            this.target = target;
            this.caller = caller;

            this.timer = timer;
            this.kawooshHeight = kawooshHeight;
        }

        @Override
        public Type<Opening> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.OPENING;
        }

        @Override
        public void toNbt(CompoundTag nbt, Context context) {
            if (target != null)
                nbt.putLong("address", target.globalAddress());
            nbt.putBoolean("caller", caller);
            nbt.putInt("timer", timer);
            nbt.putFloat("kawooshHeight", kawooshHeight);
        }
    }

    class Open implements GateState<Open> {

        public static final Type<Open> state = new Type<>(StargateAPI.modLoc("generic/open"), 0) {
            @Override
            public Open fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                long address = nbt.getLong("address"); // FIXME -1 if doesnt exist
                boolean caller = nbt.getBoolean("caller");

                return new Open(Stargate.resolveGlobal(address, context), caller);
            }
        };

        public static final int TICKS_PER_OPEN = 60 * 20;
        public static final int TELEPORT_FREQUENCY = 10;
        public static final int TELEPORT_DELAY = 20;

        public final @Nullable Stargate target;
        public final boolean caller;

        public int timer;

        public Open(@Nullable Stargate target, boolean caller) {
            this.target = target;
            this.caller = caller;
        }

        @Override
        public Type<Open> type() {
            return state;
        }

        @Override
        public StateType gateState() {
            return StateType.OPEN;
        }

        @Override
        public void toNbt(CompoundTag nbt, Context context) {
            if (target != null)
                nbt.putLong("address", target.globalAddress());
            nbt.putBoolean("caller", this.caller);
        }
    }

    enum StateType {
        CLOSED,
        OPENING,
        OPEN
    }
}