package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.state.NbtSerializer;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record PrototypeIdentityState(PrototypeRegistryEntry prototype) implements TState<PrototypeIdentityState>, NbtSerializer {

    public static final TState.NbtBacked<PrototypeIdentityState> state = new NbtBacked<>(StargateAPI.modLoc("identity"), 0) {
        @Override
        public PrototypeIdentityState fromNbt(@NotNull CompoundTag nbt, boolean isClient) {
            ResourceLocation loc = ResourceLocation.parse(nbt.getString("prototype"));

            PrototypeRegistryEntry prototype = IXplatAbstractions.INSTANCE.getPrototypeRegistry().get(loc);
            return new PrototypeIdentityState(prototype);
        }
    };

    @Override
    public Type<PrototypeIdentityState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull CompoundTag nbt, boolean isClient) {
        nbt.putString("prototype", this.prototype.key().location().toString());
    }
}
