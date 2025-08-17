package dev.amble.stargate.block.entities;

import com.mojang.serialization.DataResult;
import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
        if (this.blockSet != null) {
            nbt.put("blockSet", NbtHelper.fromBlockState(this.blockSet));
        }
        super.writeNbt(nbt);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.world instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getChunkManager().threadedAnvilChunkStorage.getPlayersWatchingChunk(new ChunkPos(this.pos))) {
                player.networkHandler.sendPacket(this.toUpdatePacket());
            }
        }
    }

    protected void sync() {
        if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
            chunkManager.markForUpdate(this.pos);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        DataResult<BlockState> blockStateDataResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet"));
        this.setBlockSet(blockStateDataResult.result().orElse(null));
        super.readNbt(nbt);
        this.sync();
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