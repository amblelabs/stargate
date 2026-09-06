package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record PrototypeIdentityState(ResourceLocation key, Prototype prototype) implements NbtState<PrototypeIdentityState> {

    public static final Type<PrototypeIdentityState> state = new Type<>(StargateAPI.modLoc("identity"), 0) {
        @Override
        public PrototypeIdentityState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            ResourceLocation loc = Objects.requireNonNull(NbtUtil.getLoc(nbt, "prototype"));
            Prototype prototype = Objects.requireNonNull(IXplatAbstractions.INSTANCE.getPrototypeRegistry().get(loc));

            return new PrototypeIdentityState(loc, prototype);
        }
    };

    @Override
    public Type<PrototypeIdentityState> type() {
        return state;
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        nbt.putString("prototype", this.key.toString());
    }
}
