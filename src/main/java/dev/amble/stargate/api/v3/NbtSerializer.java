package dev.amble.stargate.api.v3;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface NbtSerializer {
    void toNbt(NbtCompound nbt, boolean isClient);
}
