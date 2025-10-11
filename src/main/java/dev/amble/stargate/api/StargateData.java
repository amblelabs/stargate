package dev.amble.stargate.api;

import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StargateData {

    static @Nullable StargateData get(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.get(serverWorld) : null;
    }

    static @NotNull StargateData getOrCreate(World world) {
        return world instanceof ServerWorld serverWorld ? StargateServerData.getOrCreate(serverWorld) : null;
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
