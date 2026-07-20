package dev.amblelabs.stargate.api.ecs;

import dev.drtheo.ecs.state.TState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface NbtState<Self extends NbtState<Self>> extends TState<Self>, NbtSerializer {

    abstract class Type<T extends NbtState<? extends T>> extends TState.NbtBacked<T, NbtSerializer.Context, NbtDeserializer.Context> {

        public Type(@NotNull ResourceLocation id, int version, Fix... fix) {
            super(id, version, fix);
        }
    }

    abstract class GroupedType<T extends NbtState<T>> extends Type<T> {

        private final TState.Type<?> superType;

        public GroupedType(TState.Type<?> type, @NotNull ResourceLocation id, int version, Fix... fix) {
            super(id, version, fix);

            this.superType = type;
        }

        @Override
        public int verifyIndex() {
            if (this.index == -1)
                this.index = this.superType.verifyIndex();

            return super.verifyIndex();
        }
    }
}
