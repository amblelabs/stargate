package dev.amble.stargate.api.v4.state;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface NbtSerializer {
    void toNbt(NbtCompound nbt, boolean isClient);
}
