package dev.amblelabs.stargate.api.ecs;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface NbtDeserializer<T> extends dev.drtheo.ecs.state.NbtDeserializer<T, NbtDeserializer.Context> {

    /**
     * @param level
     * @param isClient whether the context is on logical client. If {@code level} is {@code null},
     *                 then it will be assumed that the code is running on logical server.
     */
    record Context(@Nullable Level level, boolean isClient, Type type) {

        public enum Type {
            CREATE,
            LOAD,
            UPDATE,
        }

        public static Builder forUpdate(Level level) {
            return new Builder().update().level(level);
        }

        public static Builder forCreate() {
            return forCreate(null);
        }

        public static Builder forCreate(@Nullable Level level) {
            return new Builder().create().level(level);
        }

        public static Builder forLoad() {
            return forLoad(null);
        }

        public static Builder forLoad(@Nullable Level level) {
            return new Builder().load().level(level);
        }

        public static class Builder {

            private @Nullable Level level;
            private @Nullable Type type;

            /**
             * Indicates that this context is used for loading the state from disk for the first time.
             */
            public Builder load() {
                this.type = Type.LOAD;
                return this;
            }

            public Builder create() {
                this.type = Type.CREATE;
                return this;
            }

            public Builder update() {
                this.type = Type.UPDATE;
                return this;
            }

            public Builder level(Level level) {
                this.level = level;
                return this;
            }

            public NbtDeserializer.Context get() {
                return new Context(this.level, this.level != null && this.level.isClientSide, Objects.requireNonNull(this.type));
            }
        }
    }
}
