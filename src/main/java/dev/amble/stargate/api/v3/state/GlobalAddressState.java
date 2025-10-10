package dev.amble.stargate.api.v3.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import org.jetbrains.annotations.NotNull;

public record GlobalAddressState(long address) implements TState<GlobalAddressState>, NbtSerializer {

    public static final Type<GlobalAddressState> state = new NbtBacked<>(StargateMod.id("address/global")) {

        @Override
        public GlobalAddressState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            NbtElement element = nbt.get("address");
            return element != null ? new GlobalAddressState(((NbtLong) element).longValue()) : new GlobalAddressState();
        }
    };

    public GlobalAddressState() {
        this(AddressProvider.Global.generate());
    }

    @Override
    public Type<GlobalAddressState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        nbt.putLong("address", address);
    }
}
