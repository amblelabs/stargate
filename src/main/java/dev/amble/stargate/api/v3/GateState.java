package dev.amble.stargate.api.v3;

import dev.amble.stargate.StargateMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * @param <From> what state group this state is from. Unused in code. Enforces proper usage by typing.
 */
@SuppressWarnings("unused")
public interface GateState<Self extends GateState<Self, From>, From extends GateStates<From>> {

    Type<Self> type();

    NbtCompound toNbt(NbtCompound nbt);

    interface Type<T extends GateState<T, ?>> extends NbtDeserializer<T> {
        Identifier id();

        default NbtCompound toNbt(T t) {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("id", id().toString());
            return t.toNbt(nbt);
        }
    }

    record StargateChildType<T extends GateState<?>>(Type type, NbtDeserializer<T> deser) implements Type<T> {

        public static <T extends GateState<?>> StargateChildType<T> closed(NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.CLOSED, deser);
        }

        public static <T extends GateState<?>> StargateChildType<T> preOpen(NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.PRE_OPEN, deser);
        }

        public static <T extends GateState<?>> StargateChildType<T> open(NbtDeserializer<T> deser) {
            return new StargateChildType<>(Type.OPEN, deser);
        }

        @Override
        public Identifier id() {
            return null;
        }

        @Override
        public NbtCompound toNbt(T t) {
            // don't put the id in, it's useless
            return t.toNbt(new NbtCompound());
        }

        @Override
        public T fromNbt(NbtCompound nbt, boolean isClient) {
            return deser.fromNbt(nbt, isClient);
        }

        enum Type {
            CLOSED {
                @Override
                public <T extends GateState<?>> StargateChildType<T> get(StargateType<T> t) {
                    return t.closed;
                }
            },
            PRE_OPEN {
                @Override
                public <T extends GateState<?>> StargateChildType<T> get(StargateType<T> t) {
                    return t.preOpen;
                }
            },
            OPEN {
                @Override
                public <T extends GateState<?>> StargateChildType<T> get(StargateType<T> t) {
                    return t.open;
                }
            };

            public abstract <T extends GateState<?>> StargateChildType<T> get(StargateType<T> t);

            public static Type byName(String name) {
                return valueOf(name);
            }
        }
    }

    record StargateType<T extends GateState<?>>(
            Identifier id, StargateChildType<T> closed, StargateChildType<T> preOpen, StargateChildType<T> open) implements Type<T> {

        private static final String TYPE_KEY = "Type";

        public StargateType(StargateChildType<T> closed, StargateChildType<T> preOpen, StargateChildType<T> open) {
            this(StargateMod.id("stargate"), closed, preOpen, open);
        }

        @Override
        public T fromNbt(NbtCompound nbt, boolean isClient) {
            String type = nbt.getString(TYPE_KEY);
            return StargateChildType.Type.byName(type).get(this).fromNbt(nbt, isClient);
        }

        @Override
        public NbtCompound toNbt(T t) {
            NbtCompound nbt = Type.super.toNbt(t);
            nbt.putString(TYPE_KEY, t.type().toString());

            return nbt;
        }
    }
}
