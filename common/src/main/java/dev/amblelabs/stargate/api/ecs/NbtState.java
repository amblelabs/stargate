package dev.amblelabs.stargate.api.ecs;

import dev.drtheo.ecs.state.TState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface NbtState<Self extends NbtState<Self>> extends TState<Self>, NbtSerializer {

    abstract class Type<T extends NbtState<T>> extends TState.NbtBacked<T, NbtSerializer.Context, NbtDeserializer.Context> {

        public Type(@NotNull ResourceLocation id, int version, Fix... fix) {
            super(id, version, fix);
        }
    }
}
