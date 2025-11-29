package dev.amble.stargate.api.state;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.IrisTierContainer;
import dev.amble.stargate.init.StargateIrisTiers;
import dev.amble.stargate.util.NbtUtil;
import dev.drtheo.yaar.state.NbtSerializer;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class IrisState implements TState<IrisState>, NbtSerializer {

    public static final Type<IrisState> state = new NbtBacked<>(StargateMod.id("iris/common")) {
        @Override
        public IrisState fromNbt(@NotNull NbtCompound nbt, boolean isClient) {
            IrisTier tier = NbtUtil.getRegistered(nbt, "tier", StargateIrisTiers.REGISTRY);
            int durability = nbt.getInt("durability");
            boolean open = nbt.getBoolean("open");

            return new IrisState(tier, durability, open);
        }
    };

    public boolean prevIrisState;
    public boolean open;

    public final IrisTier tier;
    public int durability;

    public IrisState(IrisTier tier) {
        this(tier, true);
    }

    protected IrisState(IrisTier tier, boolean open) {
        this(tier, tier.maxDurability(), open);
    }

    protected IrisState(IrisTier tier, int durability, boolean open) {
        this.tier = tier;
        this.durability = durability;

        this.prevIrisState = open;
        this.open = open;
    }

    @Override
    public Type<IrisState> type() {
        return state;
    }

    @Override
    public void toNbt(@NotNull NbtCompound nbt, boolean isClient) {
        NbtUtil.putRegistryKey(nbt, "tier", tier.getRegistryKey());
        nbt.putInt("durability", durability);
        nbt.putBoolean("open", open);
    }
}
