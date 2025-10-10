package dev.amble.stargate.api.v3.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import org.jetbrains.annotations.NotNull;

public record LocalAddressState(long address) implements TState<LocalAddressState>, NbtSerializer {

    public static final Type<LocalAddressState> state = new NbtBacked<>(StargateMod.id("address/local")) {

        @Override
        public LocalAddressState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            NbtElement element = nbt.get("address");
            return element != null ? new LocalAddressState(((NbtLong) element).longValue()) : new LocalAddressState();
        }
    };

    public LocalAddressState() {
        this(AddressProvider.Local.generate());
    }

    @Override
    public Type<LocalAddressState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putLong("address", address);
    }
}
