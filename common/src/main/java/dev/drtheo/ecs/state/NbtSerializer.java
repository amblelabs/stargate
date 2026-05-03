package dev.drtheo.ecs.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for all classes that do NBT serialization.
 * @author DrTheodor (DrTheo_)
 */
@FunctionalInterface
public interface NbtSerializer {
    /**
     * Serializes the object to NBT.
     * @implNote Mutates the {@code nbt} parameter.
     *
     * @param nbt the {@link Tag} to serialize to.
     * @param isClient whether the data is being serialized on a client.
     */
    @Contract(mutates = "param1")
    void toNbt(@NotNull CompoundTag nbt, boolean isClient);
}