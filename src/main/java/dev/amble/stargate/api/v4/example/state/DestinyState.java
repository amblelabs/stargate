package dev.amble.stargate.api.v4.example.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v4.example.Stargate;
import dev.amble.stargate.api.v4.state.NbtSerializer;
import dev.amble.stargate.api.v4.state.TState;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface DestinyState {

    static Stargate createGate(boolean created) {
        return new Stargate(created, Closed::new, Closed.state, Opening.state);
    }

    class Closed implements TState<Closed>, NbtSerializer {

        public static final Type<Closed> state = new NbtBacked<>(StargateMod.id("destiny/closed")) {
            @Override
            public @NotNull Closed decode(@NotNull NbtCompound element, boolean isClient) {
                return new Closed();
            }
        };

        @Override
        public Type<Closed> type() {
            return state;
        }

        @Override
        public void toNbt(NbtCompound nbt, boolean isClient) {

        }
    }

    class Opening implements TState<Opening>, NbtSerializer {

        public static final Type<Opening> state = new NbtBacked<>(StargateMod.id("destiny/opening")) {
            @Override
            public @NotNull Opening decode(@NotNull NbtCompound element, boolean isClient) {
                return new Opening();
            }
        };

        @Override
        public Type<Opening> type() {
            return state;
        }

        @Override
        public void toNbt(NbtCompound nbt, boolean isClient) {

        }
    }
}
