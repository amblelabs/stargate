package dev.amble.stargate.block.entities;

import com.mojang.serialization.DataResult;
import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.block.StargateBlockTickEvent;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StargateBlockEntity extends StargateLinkableBlockEntity {

	public AnimationState CHEVRON_LOCK_STATE = new AnimationState();

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

	@Nullable @Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
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
	public void readNbt(NbtCompound nbt) {
		DataResult<BlockState> blockStateDataResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockSet"));
		this.setBlockSet(blockStateDataResult.result().orElse(null));
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

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.isSneaking())
			return ActionResult.PASS;

        if (!(world.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity))
            return ActionResult.FAIL;

        ItemStack heldItem = player.getStackInHand(hand);
		BlockState newSetState = null;

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem) {
			newSetState = blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, heldItem, hit));
		}

		blockEntity.setBlockSet(newSetState);
		blockEntity.markDirty();

        return ActionResult.SUCCESS;
	}

	public void onBreak(BlockState state) {
		if (this.hasStargate()) {
			Stargate gate = this.gate().get();
			Direction facing = state.get(StargateBlock.FACING);

			gate.shape().destroy(world, pos, facing);
			gate.dispose();
		}

		this.ref = null;
	}

	public void onPlacedWithKernel(World world, BlockPos pos, GateKernelRegistry.Entry entry) {
		if (world.isClient()) return;

		this.requiresPlacement = false;

		Direction facing = world.getBlockState(pos).get(StargateBlock.FACING);
		DirectedGlobalPos globalPos = DirectedGlobalPos.create(world.getRegistryKey(), this.getPos(), DirectedGlobalPos.getGeneralizedRotation(facing));

		Stargate stargate = entry.creator().create(globalPos);
		ServerStargateNetwork.get().add(stargate);

		this.setStargate(new StargateRef(stargate));

		stargate.shape().place(StargateBlocks.RING.getDefaultState(), world, this.pos, facing);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		if (this.isLinked())
			this.gate().ifPresent(stargate -> TEvents.handle(new StargateBlockTickEvent(stargate, this, world, pos, state)));

		if (world.isClient()) age++;
	}
}
