package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StargateRingBlockEntity extends BlockEntity {

    static final String ID_BLOCK = "Block";

    private @Nullable BlockState blockSet;

    public StargateRingBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.RING, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (this.blockSet != null) tag.put(ID_BLOCK, NbtUtils.writeBlockState(this.blockSet));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(StargateRingBlockEntity.ID_BLOCK))
            this.blockSet = NbtUtils.readBlockState(registries.asGetterLookup().lookupOrThrow(Registries.BLOCK), tag.getCompound(ID_BLOCK));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null || level.isClientSide()) return;

        BlockState state = this.getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    public void setBlockSet(@Nullable BlockState state) {
        this.blockSet = state;
        this.setChanged();
    }

    public @Nullable BlockState getBlockSet() {
        return this.blockSet;
    }
}
