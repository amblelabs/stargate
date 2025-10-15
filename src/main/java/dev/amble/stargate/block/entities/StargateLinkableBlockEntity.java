package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.lib.blockentity.StructurePlaceableBlockEntity;
import dev.amble.stargate.api.StargateData;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class StargateLinkableBlockEntity extends ABlockEntity implements StargateLinkable, StructurePlaceableBlockEntity {

	protected long address = -1;
	protected Stargate stargate;

	public StargateLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @Nullable Stargate asGate() {
		return stargate != null ? stargate : StargateData.apply(world, data -> data.getById(address));
	}

	@Override
	public boolean isLinked() {
		return address != -1 && StargateLinkable.super.isLinked();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

        if (!nbt.contains("Address")) return;
		this.link(nbt.getLong("Address"));
    }

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

        if (!this.isLinked()) return;
        nbt.putLong("Address", this.address);
    }

	@Override
	public void link(@Nullable Stargate gate) {
		Stargate oldGate = this.stargate;

		this.stargate = gate;
		this.address = gate != null ? StargateServerData.getAnyId(gate) : -1;

		if (world instanceof ServerWorld serverWorld) {
			StargateServerData data = StargateServerData.getOrCreate(serverWorld);

			if (oldGate != null)
				data.unmark(oldGate);

			if (!this.isLinked()) return;
			data.mark(this);
		} else if (!this.isLinked()) return;

		this.markDirty();
		this.sync();
	}

	@Override
	public void link(long address) {
		Stargate stargate = StargateData.apply(world, data -> data.getById(address));
		this.link(stargate);
	}

	@Override
	public void onBreak(BlockState state, World world, BlockPos pos, BlockState newState) {
		this.link(null); // frees the dangling stargate mark
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
