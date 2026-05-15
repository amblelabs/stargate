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
public interface NbtDeserializer<T, Context> {

    /**
     * Serializes the object to NBT.
     *
     * @param nbt the {@link Tag} to deserialize from.
     * @param context the context of deserialization.
     * @return the deserialized object's instance.
     */
    @Contract(pure = true)
    T fromNbt(@NotNull CompoundTag nbt, Context context);
}