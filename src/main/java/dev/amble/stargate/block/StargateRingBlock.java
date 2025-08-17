package dev.amble.stargate.block;

import dev.amble.lib.api.ICantBreak;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class StargateRingBlock extends BlockWithEntity implements ICantBreak, Waterloggable {
	public static final BooleanProperty WATERLOGGED;
	public StargateRingBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return super.getOutlineShape(state, world, pos, context);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof StargateRingBlockEntity ringBlockEntity) {
			ItemStack heldItem = player.getStackInHand(hand);
			if (!heldItem.isEmpty()) {
				if (heldItem.getItem() instanceof BlockItem blockItem) {
					if (blockItem.getBlock().getDefaultState() != ringBlockEntity.getBlockSet()) {
						ringBlockEntity.setBlockSet(blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getMainHandStack(), hit)));
						ringBlockEntity.markDirty();
						return ActionResult.SUCCESS;
					}
					return ActionResult.FAIL;
				}
			} else {
				ringBlockEntity.setBlockSet(null);
				ringBlockEntity.markDirty();
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
		return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	static {
		WATERLOGGED = Properties.WATERLOGGED;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StargateRingBlockEntity(pos, state);
	}
}
