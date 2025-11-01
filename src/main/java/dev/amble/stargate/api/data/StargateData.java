package dev.amble.stargate.api.data;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.service.StargateDataProviderService;
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
        return StargateDataProviderService.INSTANCE.get(world);
    }

    static void accept(World world, Consumer<StargateData> consumer) {
        StargateDataProviderService.INSTANCE.accept(world, consumer);
    }

    static <R> @Nullable R apply(World world, Function<StargateData, R> func) {
        return StargateDataProviderService.INSTANCE.apply(world, func);
    }

    static @NotNull StargateData getOrCreate(World world) {
        return StargateDataProviderService.INSTANCE.getOrCreate(world);
    }

    default void removeLocal(long address) {
        removeId(AddressProvider.Local.getId(address));
    }

    default void removeGlobal(long address) {
        removeId(AddressProvider.Global.getId(address));
    }

    default void addLocal(long address, Stargate stargate) {
        addId(AddressProvider.Local.getId(address), stargate);
    }

    default void addGlobal(long address, Stargate stargate) {
        addId(AddressProvider.Global.getId(address), stargate);
    }

    default @Nullable Stargate getLocal(long address) {
        return getById(AddressProvider.Local.getId(address));
    }

    default @Nullable Stargate getGlobal(long address) {
        return getById(AddressProvider.Global.getId(address));
    }

    void addId(long id, Stargate stargate);
    void removeId(long id);
    boolean containsId(long id);
    @Nullable Stargate getById(long id);
}
