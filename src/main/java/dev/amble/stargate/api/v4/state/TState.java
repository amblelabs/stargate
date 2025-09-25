package dev.amble.stargate.api.v4.state;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface TState<Self extends TState<Self>> {

    Type<Self> type();

    abstract class NbtBacked<T extends TState<T> & NbtSerializer> extends SerializableType<T> {

        public NbtBacked(@NotNull Identifier id) {
            super(id);
        }

        @Override
        public @NotNull NbtCompound encode(@NotNull T t, boolean isClient) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", this.id.toString());

            t.toNbt(nbt, isClient);
            return nbt;
        }
    }

    abstract class SerializableType<T extends TState<T>> extends Type<T> {

        public SerializableType(@NotNull Identifier id) {
            super(id);
        }

        public abstract @NotNull T decode(@NotNull NbtCompound element, boolean isClient);

        public abstract @NotNull NbtCompound encode(@NotNull T t, boolean isClient);
    }

    @SuppressWarnings("unused")
    class Type<T extends TState<T>> {

        protected int index = -1;
        protected final @NotNull Identifier id;

        public Type(@NotNull Identifier id) {
            this.id = id;
        }

        public @NotNull Identifier id() {
            return id;
        }

        public int verifyIndex() {
            if (index < 0)
                throw new IllegalStateException();

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
