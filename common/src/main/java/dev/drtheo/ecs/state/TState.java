package dev.drtheo.ecs.state;

import dev.drtheo.ecs.event.TEventsRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base interface for all states.
 *
 * @param <Self> itself.
 * @author DrTheodor (DrTheo_)
 */
public interface TState<Self extends TState<Self>> {

    /**
     * @return the type of the state.
     */
    @Contract(pure = true)
    Type<Self> type();

    /**
     * A {@link SerializableType} backed by NBT.
     * @implNote All implementations must implement {@link NbtSerializer} for their {@link TState}.
     *
     * @param <T> the state.
     */
    abstract class NbtBacked<T extends TState<T> & NbtSerializer> extends SerializableType<T, CompoundTag> implements NbtDeserializer<T> {

        public static final String VERSION_TAG = "DataVersion";

        private final int version;
        private final Fix[] fixes;

        @Contract(pure = true)
        public NbtBacked(@NotNull ResourceLocation id, int version, Fix... fix) {
            super(id);

            this.version = version;
            this.fixes = fix;
        }

        @Override
        @Contract(pure = true)
        public @Nullable CompoundTag encode(@NotNull T t, boolean isClient) {
            CompoundTag nbt = new CompoundTag();
            t.toNbt(nbt, isClient);

            nbt.putInt(VERSION_TAG, this.version);
            return nbt;
        }

        @Override
        @Contract(pure = true)
        public @NotNull T decode(@NotNull CompoundTag element, boolean isClient) {
            try {
                return this.fromNbt(element, isClient);
            } catch (Exception e) {
                TEventsRegistry.LOGGER.info(element.toString());
                throw e;
            }
        }

        public CompoundTag update(CompoundTag tag, int defaultVersion) {
            int version = tag.contains(VERSION_TAG, Tag.TAG_ANY_NUMERIC) ? tag.getInt(VERSION_TAG) : defaultVersion;
            return update(version, tag);
        }

        public CompoundTag update(int version, CompoundTag tag) {
            if (version < this.version) {
                for (Fix fix : fixes) {
                    if (version <= fix.version) {
                        tag = fix.fixer().update(tag);
                    }
                }
            }

            return tag;
        }

        public record Fix(int version, Fixer fixer) { }

        public interface Fixer {
            CompoundTag update(CompoundTag tag);
        }
    }

    /**
     * A state backed by something serializable. All state that needs to be serialized (persisted and/or synced) must use
     * this state type.
     *
     * @param <T> the state.
     * @see NbtBacked
     */
    abstract class SerializableType<T extends TState<T>, S> extends Type<T> {

        /**
         * Constructs a new state type with the provided {@link ResourceLocation}, which is later used for registration.
         *
         * @param id the state's {@link ResourceLocation}.
         */
        @Contract(pure = true)
        public SerializableType(@NotNull ResourceLocation id) {
            super(id);
        }

        /**
         * Decodes the object and creates a new instance.
         *
         * @param s serialized data.
         * @param isClient whether the deserialization is running on client.
         * @return a new {@link T} instance, containing all the deserialized data.
         */
        @Contract(pure = true)
        public abstract @NotNull T decode(@NotNull S s, boolean isClient);

        /**
         * Encodes the object.
         *
         * @param t the unserialized state.
         * @param isClient whether the serialization is running on client.
         * @return a new {@link S} instance, containing all the serialized data, or {@code null}, to skip serialization.
         */
        @Contract(pure = true)
        public abstract @Nullable S encode(@NotNull T t, boolean isClient);
    }

    /**
     * Base type for {@link TState}s. Used in registration.
     *
     * @param <T> the state.
     */
    @SuppressWarnings("unused")
    class Type<T extends TState<T>> {

        protected int index = -1;
        protected final @NotNull ResourceLocation id;

        /**
         * Constructs a new state type with the provided {@link ResourceLocation}, which is later used for registration.
         *
         * @param id the state's {@link ResourceLocation}.
         */
        @Contract(pure = true)
        public Type(@NotNull ResourceLocation id) {
            this.id = id;
        }

        /**
         * @return the state's {@link ResourceLocation}.
         */
        @Contract(pure = true)
        public @NotNull ResourceLocation id() {
            return id;
        }

        /**
         * Verifies the registration index and throws an exception in case of failure.
         * Used for quick access of {@link TState}s by {@link TStateContainer}s.
         *
         * @return the {@link Type}'s index.
         * @throws IllegalStateException if the {@link Type} is not registered.
         */
        @Contract(pure = true)
        public int verifyIndex() {
            if (index < 0)
                throw new IllegalStateException("State " + id + " is not registered!");

            return index;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "index=" + index +
                    ", id=" + id +
                    '}';
        }
    }
}