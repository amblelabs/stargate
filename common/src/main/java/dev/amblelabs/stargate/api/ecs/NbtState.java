package dev.amblelabs.stargate.api.ecs;

import dev.drtheo.ecs.state.TState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface NbtState<Self extends NbtState<Self>> extends TState<Self>, NbtSerializer {

    abstract class Type<T extends NbtState<? extends T>> extends TState.NbtBacked<T, NbtSerializer.Context, NbtDeserializer.Context> {

        public Type(@NotNull ResourceLocation id, int version, Fix... fix) {
            super(id, version, fix);
        }
    }

    abstract class GroupedType<T extends NbtState<T>> extends Type<T> {

        private final Supplier<TState.Type<?>> superType;

        public GroupedType(Supplier<TState.Type<?>> type, @NotNull ResourceLocation id, int version, Fix... fix) {
            super(id, version, fix);

            this.superType = type;
        }

        @Override
        public int verifyIndex() {
            return superType.get().verifyIndex();
        }
    }
}
