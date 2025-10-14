package dev.amble.stargate.block.entities;

import com.mojang.serialization.DataResult;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.block.StargateBlockTickEvent;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class StargateBlockEntity extends StargateLinkableBlockEntity {

	// TODO: move to a client state
	public int age;

	public boolean requiresPlacement = false;
	private BlockState blockSet;

	public StargateBlockEntity(BlockPos pos, BlockState state) {
		super(StargateBlockEntities.GENERIC_STARGATE, pos, state);
	}

	public StargateBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		if (this.blockSet != null) {
			nbt.put("blockSet", NbtHelper.fromBlockState(this.blockSet));
		}
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		DataResult<BlockState> blockStateDataResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet"));
		this.setBlockSet(blockStateDataResult.result().orElse(null));
		super.readNbt(nbt);
	}

	@Override
	public void amble$onStructurePlaced(NbtCompound nbt) {
		super.amble$onStructurePlaced(nbt);
	}

	public void setBlockSet(BlockState state) {
		this.blockSet = state;
		this.markDirty();
		this.sync();
	}

	public BlockState getBlockSet() {
		return this.blockSet;
	}

	public void onBreak(BlockState state) {
		this.acceptGate(gate -> gate.remove((ServerWorld) world));
		this.unlink();
	}

	public void onPlacedWithKernel(World world, BlockPos pos, GateKernelRegistry.Entry entry) {
		if (!(world instanceof ServerWorld serverWorld)) return;

		this.requiresPlacement = false;

		Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);

		Stargate stargate = entry.create(serverWorld, this.getPos(), facing);
		this.link(stargate);
	}

	public static void tick(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		((StargateBlockEntity) blockEntity).internalTick(world, pos, state);
	}

	private void internalTick(World world, BlockPos pos, BlockState state) {
		this.acceptGate(stargate -> TEvents.handle(
				new StargateBlockTickEvent(stargate, this, world, pos, state)
		));

		if (world.isClient()) age++;
	}
}
