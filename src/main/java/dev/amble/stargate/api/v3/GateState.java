package dev.amble.stargate.api.v3;

import dev.amble.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface GateState<Self extends GateState<Self>> {

    Type<Self> type();

    void toNbt(NbtCompound nbt);

    interface Type<T extends GateState<T>> extends NbtDeserializer<T> {
        Key key();

        default Identifier id() {
            return key().id;
        }

        class Key {
            final Identifier id;
            int index;

            Key(Identifier id) {
                this.id = id;
            }
        }

        default NbtCompound toNbt(T t) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", id().toString());

            t.toNbt(nbt);
            return nbt;
        }

        default @Nullable Group group() {
            return null;
        }
    }

    record StargateChildType<T extends GateState<T>>(Key key, Type type, Group group, NbtDeserializer<T> deser) implements Type<T> {

        public static <T extends GateState<T>> StargateChildType<T> closed(Group group, NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.CLOSED, group, deser);
        }

        public static <T extends GateState<T>> StargateChildType<T> preOpen(Group group, NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.PRE_OPEN, group, deser);
        }

        public static <T extends GateState<T>> StargateChildType<T> open(Group group, NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.OPEN, group, deser);
        }

        private StargateChildType(Type type, Group group, NbtDeserializer<T> deser) {
            this(new Key(StargateMod.id("stargate/" + type.path())), type, group, deser);

            group.types().add(this);
        }

        @Override
        public NbtCompound toNbt(T t) {
            // don't put the id in, it's useless
            NbtCompound result = new NbtCompound();
            t.toNbt(result);

            return result;
        }

        @Override
        public T fromNbt(NbtCompound nbt, boolean isClient) {
            return deser.fromNbt(nbt, isClient);
        }

        enum Type {
            CLOSED("closed"),
            PRE_OPEN("pre_open"),
            OPEN("open");

            private final String path;

            Type(String path) {
                this.path = path;
            }

            public String path() {
                return path;
            }
        }
    }

    interface Group {

        List<Type<?>> types();

        @Contract(pure = true)
        static Mutable create(Type<?>... types) {
            ArrayList<Type<?>> list = new ArrayList<>();
            Collections.addAll(list, types);

            return new Mutable(list);
        }

        record Mutable(ArrayList<Type<?>> types) implements Group { }

        default void removeAll(GateStateHolder<?> holder) {
            for (Type<?> type : types()) {
                holder.removeState(type);
            }
        }
    }
}
