package dev.amble.stargate.block.entities;

import dev.amble.lib.blockentity.StructurePlaceableBlockEntity;
import dev.amble.stargate.api.StargateData;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class StargateLinkableBlockEntity extends BlockEntity implements StargateLinkable, StructurePlaceableBlockEntity {

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
	public void link(@NotNull Stargate gate) {
		Objects.requireNonNull(gate);

		if (this.address == -1)
			this.address = StargateServerData.getAnyId(gate);

		this.stargate = gate;

		if (!this.isLinked()) return;

		if (world instanceof ServerWorld serverWorld)
			StargateServerData.get(serverWorld).mark(this);

		this.markDirty();
		this.sync();
	}

	@Override
	public void link(long address) {
		Stargate stargate = StargateData.get(world).getById(address);

		if (stargate != null)
			this.address = address;

		this.link(stargate);
	}

	@Override
	public void unlink() {
		this.address = -1;
		this.stargate = null;
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		if (world instanceof ServerWorld serverWorld)
			StargateServerData.accept(serverWorld, data -> data.mark(this));

		return createNbt();
	}

	@Nullable @Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void amble$onStructurePlaced(NbtCompound nbt) {
		nbt.remove("Address");
	}

	protected void sync() {
		if (this.world != null && this.world.getChunkManager() instanceof ServerChunkManager chunkManager)
			chunkManager.markForUpdate(this.pos);
	}
}
