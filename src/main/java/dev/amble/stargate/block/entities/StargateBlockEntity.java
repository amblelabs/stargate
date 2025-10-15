package dev.amble.stargate.block.entities;

import com.mojang.serialization.DataResult;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.block.StargateBlockTickEvent;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.item.StargateItem;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
		this.requiresPlacement = true;
	}

	public void setBlockSet(BlockState state) {
		this.blockSet = state;

		this.markDirty();
		this.sync();
	}

	public BlockState getBlockSet() {
		return this.blockSet;
	}

	@Override
	public void onBreak(BlockState state, World world, BlockPos pos, BlockState newState) {
		this.acceptGate(gate -> gate.remove((ServerWorld) world));
		this.link(null);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (!(world instanceof ServerWorld serverWorld) || !(stack.getItem() instanceof StargateItem item)) return;

		this.requiresPlacement = false;

		Direction facing = HorizontalBlockBehavior.getFacing(state);
		Stargate stargate = item.getCreator().create(serverWorld, pos, facing);

		this.link(stargate);
	}

	@Override
	public void tick(World world, BlockPos blockPos, BlockState blockState) {
		this.acceptGate(stargate -> TEvents.handle(
				new StargateBlockTickEvent(stargate, this, world, blockPos, blockState)
		));

		if (world.isClient()) age++;
	}
}
