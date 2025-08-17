package dev.amble.stargate.block.entities;

import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.network.StargateRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;

public abstract class StargateLinkableBlockEntity extends BlockEntity implements StargateLinkable {
	protected StargateRef ref;

	public StargateLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

        if (!nbt.contains("Address")) return;

        this.ref = StargateRef.createAs(this, nbt.getUuid("Address"));
        this.markDirty();
        this.sync();
    }

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

        if (this.ref == null) return;

        nbt.putUuid("Address", this.ref.id());
    }

	@Override
	public StargateRef gate() {
		if (this.ref == null) return null;

		return this.ref;
	}

	@Override
	public void setStargate(StargateRef gate) {
		this.ref = gate;
		this.markDirty();
		this.sync();
	}

	protected void sync() {
		if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
			chunkManager.markForUpdate(this.pos);
	}
}
