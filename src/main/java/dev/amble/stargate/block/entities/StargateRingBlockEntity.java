package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;

public class StargateRingBlockEntity extends ABlockEntity {

    private BlockState blockSet;

    public StargateRingBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.RING, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        if (this.blockSet != null) nbt.put("blockSet", NbtHelper.fromBlockState(this.blockSet));

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.blockSet = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet")).result().orElse(null);

        super.readNbt(nbt);
    }

    public void setBlockSet(BlockState state) {
        this.blockSet = state;

        this.markDirty();
        this.sync();
    }

    public BlockState getBlockSet() {
        return this.blockSet;
    }
}