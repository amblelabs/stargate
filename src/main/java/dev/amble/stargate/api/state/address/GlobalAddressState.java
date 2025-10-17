package dev.amble.stargate.api.state.address;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.AddressProvider;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

public record GlobalAddressState(long address) implements TState<GlobalAddressState>, NbtSerializer, LongSupplier {

    public static final Type<GlobalAddressState> state = new NbtBacked<>(StargateMod.id("address/global")) {

        @Override
        public GlobalAddressState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            return new GlobalAddressState(nbt.getLong("address"));
        }
    };

    public GlobalAddressState(RegistryKey<World> world, BlockPos pos) {
        this(AddressProvider.Global.generate(world));
    }

    @Override
    public Type<GlobalAddressState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putLong("address", address);
    }

    @Override
    public long getAsLong() {
        return AddressProvider.Global.getId(address);
    }

    @Override
    public @NotNull String toString() {
        return "G:" + AddressProvider.Global.asString(this.address);
    }
}
