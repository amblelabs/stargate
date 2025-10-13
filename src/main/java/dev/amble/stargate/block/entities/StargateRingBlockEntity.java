package dev.amble.stargate.block.entities;

import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class StargateRingBlockEntity extends BlockEntity {

    private BlockState blockSet;

    public StargateRingBlockEntity(BlockPos pos, BlockState state) {
        super(StargateBlockEntities.RING, pos, state);
    }

    @Override
    public @Nullable Object getRenderData() {
        return this;
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

    protected void sync() {
        if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
            chunkManager.markForUpdate(this.pos);
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
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