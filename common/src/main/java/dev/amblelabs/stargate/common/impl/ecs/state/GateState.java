package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.address.Glyph;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public sealed interface GateState<T extends GateState<T>> extends NbtState<T> {

    // FIXME: this stinks.
    static GateState<?> findAny(Stargate stargate) {
        GateState<?> state = stargate.stateOrNull(Closed.state);

        if (state != null) return state;

        state = stargate.stateOrNull(Opening.state);

        if (state != null) return state;

        state = stargate.stateOrNull(Open.state);
        if (state != null) return state;

        state = new Closed();
        stargate.addState(state);

        return state;
    }

    final class Holder implements TState<Holder> {

        public static final Type<Holder> state = new Type<>(StargateAPI.modLoc("generic/holder"));

        public TState.Type<?> current;

        public static Holder forStargate(Stargate stargate) {
            return new Holder(GateState.findAny(stargate).type());
        }

        public Holder(TState.Type<?> current) {
            this.current = current;
        }

        @Override
        public Type<Holder> type() {
            return state;
        }
    }

    final class Closed implements GateState<Closed> {

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
                Stargate target = null;
                if (nbt.hasUUID("address"))
                    target = ServerStargateNetwork.GLOBAL.get(nbt.getUUID("address"));

                boolean caller = nbt.getBoolean("caller");
                int timer = nbt.getInt("timer");

                return new Opening(target, caller, timer);
            }
        };

        public static final int TICKS_PER_KAWOOSH = 4 * 20;

        public final @Nullable Stargate target;
        public final boolean caller;

        public int timer;

        public Opening(@Nullable Stargate target, boolean caller) {
            this(target, caller, 0);
        }

        private Opening(@Nullable Stargate target, boolean caller, int timer) {
            this.target = target;
            this.caller = caller;

            this.timer = timer;
        }

        @Override
        public Type<Opening> type() {
            return state;
        }

        @Override
        public void toNbt(CompoundTag nbt, Context context) {
            if (target != null)
                nbt.putUUID("address", target.getId());

            nbt.putBoolean("caller", caller);
            nbt.putInt("timer", timer);
        }
    }

    final class Open implements GateState<Open> {

        public static final Type<Open> state = new Type<>(StargateAPI.modLoc("generic/open"), 0) {
            @Override
            public Open fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                UUID address = nbt.getUUID("address");
                boolean caller = nbt.getBoolean("caller");

                return new Open(ServerStargateNetwork.GLOBAL.get(address), caller);
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
        public void toNbt(CompoundTag nbt, Context context) {
            if (target != null)
                nbt.putUUID("address", target.getId());

            nbt.putBoolean("caller", this.caller);
        }
    }
}