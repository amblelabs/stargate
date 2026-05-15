package dev.amblelabs.stargate.api.ecs;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface NbtDeserializer<T> extends dev.drtheo.ecs.state.NbtDeserializer<T, NbtDeserializer.Context> {

    record Context(boolean isClient) {

        public static Context fromLevel(@Nullable Level level) {
            return new Context(level != null && level.isClientSide());
        }
    }
}
