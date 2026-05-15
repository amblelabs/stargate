package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.NbtState;
import dev.amblelabs.stargate.api.util.NbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class IrisState implements NbtState<IrisState> {

    public static final Type<IrisState> state = new Type<>(StargateAPI.modLoc("iris"), 0) {
        @Override
        public IrisState fromNbt(CompoundTag nbt, NbtDeserializer.Context context) {
            return new IrisState(Objects.requireNonNull(NbtUtil.getLoc(nbt, "type")), nbt.getInt("durability"), nbt.getBoolean("closed"));
        }
    };

    public final ResourceLocation type;

    /**
     * Goes from positive number to 0
     */
    public int durability;

    public boolean closed;

    public IrisState(ResourceLocation type, int durability, boolean closed) {
        this.type = type;
        this.durability = durability;
        this.closed = closed;
    }

    public IrisState(ResourceLocation type, int durability) {
        this(type, durability, false);
    }

    @Override
    public void toNbt(CompoundTag nbt, NbtSerializer.Context context) {
        nbt.putString("type", type.toString());
        nbt.putInt("durability", durability);
        nbt.putBoolean("closed", closed);
    }

    @Override
    public Type<IrisState> type() {
        return state;
    }
}
