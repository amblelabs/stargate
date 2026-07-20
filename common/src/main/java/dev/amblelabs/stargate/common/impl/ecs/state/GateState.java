package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.state.TAbstractStateRegistry;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public sealed interface GateState<T extends GateState<T>> extends NbtState<T> {

    static void register(TAbstractStateRegistry registry) {
        registry.register(state);
        registry.add(Closed.state);
        registry.add(Opening.state);
        registry.add(Open.state);
    }

    Type<GateState<?>> state = new Type<>(StargateAPI.modLoc("generic/holder"), 0) {

        @Override
        public GateState<?> fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            throw new IllegalStateException();
        }
    };

    final class Closed implements GateState<Closed> {

        private static final Type<Closed> state = new GroupedType<>(GateState.state, StargateAPI.modLoc("generic/closed"), 0) {

            @Override
            public Closed fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                if (nbt.isEmpty()) return new Closed();
                return new Closed(nbt);
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

        private static final Type<Opening> state = new GroupedType<>(GateState.state, StargateAPI.modLoc("generic/opening"), 0) {

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

        private static final Type<Open> state = new GroupedType<>(GateState.state, StargateAPI.modLoc("generic/open"), 0) {

            @Override
            public Open fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
                UUID address = nbt.getUUID("address");
                boolean caller = nbt.getBoolean("caller");

                return new Open(ServerStargateNetwork.GLOBAL.get(address), caller);
            }
        };

        public static final int TICKS_PER_OPEN = 60 * 20;
        public static final int TELEPORT_FREQUENCY = 5;
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