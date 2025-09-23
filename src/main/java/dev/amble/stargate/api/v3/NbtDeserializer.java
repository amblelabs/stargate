package dev.amble.stargate.api.v3;

import net.minecraft.nbt.NbtCompound;

@FunctionalInterface
public interface NbtDeserializer<T> {
    T fromNbt(NbtCompound nbt, boolean isClient);
}
