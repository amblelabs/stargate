package dev.amble.stargate.api.v4.state;

import net.minecraft.nbt.NbtCompound;

/**
 * An interface for all classes that do NBT serialization.
 */
@FunctionalInterface
public interface NbtSerializer {
    /**
     * Serializes the object to NBT.
     * @implNote Mutates the {@code nbt} parameter.
     *
     * @param nbt the {@link NbtCompound} to serialize to.
     * @param isClient whether the data is being serialized on a client.
     */
    void toNbt(NbtCompound nbt, boolean isClient);
}
