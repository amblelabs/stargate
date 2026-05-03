package dev.drtheo.ecs.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for all classes that do NBT deserialization.
 *
 * @param <T> the target of deserialization.
 * @author DrTheodor (DrTheo_)
 */
@FunctionalInterface
public interface NbtDeserializer<T> {

    /**
     * Serializes the object to NBT.
     *
     * @param nbt the {@link Tag} to deserialize from.
     * @param isClient whether the deserialization is happening on a client.
     * @return the deserialized object's instance.
     */
    @Contract(pure = true)
    T fromNbt(@NotNull CompoundTag nbt, boolean isClient);
}