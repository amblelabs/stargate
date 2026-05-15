package dev.amblelabs.stargate.api.ecs;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface NbtSerializer extends dev.drtheo.ecs.state.NbtSerializer<NbtSerializer.Context> {

    record Context(boolean isClient) {

        public static Context fromLevel(@Nullable Level level) {
            return new Context(level != null && level.isClientSide());
        }
    }
}
