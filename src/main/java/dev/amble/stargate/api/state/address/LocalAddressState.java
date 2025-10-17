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

public record LocalAddressState(long address) implements TState<LocalAddressState>, NbtSerializer, LongSupplier {

    public static final Type<LocalAddressState> state = new NbtBacked<>(StargateMod.id("address/local")) {

        @Override
        public LocalAddressState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            return new LocalAddressState(nbt.getLong("address"));
        }
    };

    public LocalAddressState(RegistryKey<World> world, BlockPos pos) {
        this(AddressProvider.Local.generate(world));
    }

    @Override
    public Type<LocalAddressState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putLong("address", address);
    }

    @Override
    public long getAsLong() {
        return AddressProvider.Local.getId(address);
    }
}
