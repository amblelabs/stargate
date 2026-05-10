package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.util.NbtUtil;
import dev.drtheo.ecs.state.NbtSerializer;
import dev.drtheo.ecs.state.TState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class IrisState implements TState<IrisState>, NbtSerializer {

    public static final Type<IrisState> state = new NbtBacked<>(StargateAPI.modLoc("iris"), 0) {
        @Override
        public IrisState fromNbt(CompoundTag nbt, boolean isClient) {
            return new IrisState(NbtUtil.getLoc(nbt, "type"), nbt.getInt("durability"), nbt.getBoolean("closed"));
        }
    };

    public ResourceLocation type;

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
    public void toNbt(CompoundTag nbt, boolean isClient) {
        nbt.putString("type", type.toString());
        nbt.putInt("durability", durability);
        nbt.putBoolean("closed", closed);
    }

    @Override
    public Type<IrisState> type() {
        return state;
    }
}
