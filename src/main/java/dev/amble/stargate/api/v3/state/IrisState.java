package dev.amble.stargate.api.v3.state;

import dev.amble.stargate.StargateMod;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class IrisState implements TState<IrisState>, NbtSerializer {

    public static final Type<IrisState> state = new NbtBacked<>(StargateMod.id("iris/common")) {
        @Override
        public IrisState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            return null;
        }
    };

    public boolean prevIrisState;
    public boolean open;

    @Override
    public Type<IrisState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {

    }
}
