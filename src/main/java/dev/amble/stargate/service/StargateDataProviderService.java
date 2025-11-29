package dev.amble.stargate.service;

import dev.amble.stargate.api.data.StargateData;
import dev.amble.stargate.api.data.StargateServerData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface StargateDataProviderService {

    default StargateData<?> getClient() {
        throw new RuntimeException(new IllegalAccessException("Tried to access client stargate data on server!"));
    }

    default @Nullable StargateData<?> get(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.get(serverWorld) : getClient();
    }

    default void accept(World world, Consumer<StargateData<?>> consumer) {
        if (world instanceof ServerWorld serverWorld) {
            StargateServerData.accept(serverWorld, consumer::accept);
        } else {
            consumer.accept(getClient());
        }
    }

    default <R> @Nullable R apply(World world, Function<StargateData<?>, R> func) {
        if (world instanceof ServerWorld serverWorld) {
            return StargateServerData.apply(serverWorld, func::apply);
        } else {
            return func.apply(getClient());
        }
    }

    default @NotNull StargateData<?> getOrCreate(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.getOrCreate(serverWorld) : getClient();
    }
}
