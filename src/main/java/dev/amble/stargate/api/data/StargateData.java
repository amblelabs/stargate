package dev.amble.stargate.api.data;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.gates.Stargate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface StargateData {

    Identifier SYNC = StargateMod.id("sync");
    Identifier SYNC_ALL = StargateMod.id("sync_all");
    Identifier REMOVE = StargateMod.id("remove");

    static @Nullable StargateData get(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.get(serverWorld) : StargateClientData.get();
    }

    static void accept(World world, Consumer<StargateData> consumer) {
        if (world instanceof ServerWorld serverWorld) {
            StargateServerData.accept(serverWorld, consumer::accept);
        } else {
            consumer.accept(StargateClientData.get());
        }
    }

    static <R> @Nullable R apply(World world, Function<StargateData, R> func) {
        if (world instanceof ServerWorld serverWorld) {
            return StargateServerData.apply(serverWorld, func::apply);
        } else {
            return func.apply(StargateClientData.get());
        }
    }

    static @NotNull StargateData getOrCreate(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.getOrCreate(serverWorld) : StargateClientData.get();
    }

    default void removeLocal(long address) {
        removeId(AddressProvider.Local.getId(address));
    }

    default void removeGlobal(long address) {
        removeId(AddressProvider.Global.getId(address));
    }

    default @Nullable Stargate getLocal(long address) {
        return getById(AddressProvider.Local.getId(address));
    }

    default @Nullable Stargate getGlobal(long address) {
        return getById(AddressProvider.Global.getId(address));
    }

    void removeId(long id);
    boolean containsId(long id);
    @Nullable Stargate getById(long id);
}
