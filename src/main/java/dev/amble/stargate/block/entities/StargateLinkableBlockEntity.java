package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.lib.blockentity.StructurePlaceableBlockEntity;
import dev.amble.stargate.api.data.StargateRef;
import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.data.StargateLinkable;
import dev.amble.stargate.api.Stargate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class StargateLinkableBlockEntity extends ABlockEntity implements StargateLinkable, StructurePlaceableBlockEntity {

	protected final StargateRef ref = new StargateRef(this::getWorld);

	public StargateLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @Nullable Stargate asGate() {
		return ref.asGate();
	}

	@Override
	public boolean isLinked() {
		return ref.isLinked();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		this.ref.readNbt(nbt);
    }

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		this.ref.writeNbt(nbt);
    }

	@Override
	public boolean link(@Nullable Stargate gate) {
		if (!ref.link(gate))
			return false;

		this.markDirty();
		this.sync();
		return true;
	}

	@Override
	public boolean link(long address) {
		if (!ref.link(address)) return false;

		this.markDirty();
		this.sync();
		return true;
	}

	@Override
	public void unlink() {
		ref.unlink();
		this.markDirty();
		this.sync();
	}

	@Override
	public void onBreak(BlockState state, World world, BlockPos pos, BlockState newState) {
		this.unlink(); // frees the dangling stargate mark
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		if (world instanceof ServerWorld serverWorld)
			StargateServerData.accept(serverWorld, data -> data.mark(this));

		return createNbt();
	}

    @Override
	public void amble$onStructurePlaced(NbtCompound nbt) {
		nbt.remove("Address");
	}
}
