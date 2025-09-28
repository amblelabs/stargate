package dev.amble.stargate.block;

import dev.amble.stargate.api.v3.event.block.StargateBlockTickEvent;
import dev.amble.stargate.api.v3.state.IrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.item.StargateItem;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class StargateBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable {

	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public StargateBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

		return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER)
				.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND || !(world.getBlockEntity(pos) instanceof StargateBlockEntity be) || !be.isLinked())
            return ActionResult.PASS;

		if (!player.isSneaking()) {
			if (world.isClient()) return ActionResult.SUCCESS;

			// TODO: silly, move this to a result event
			return be.gate().apply(stargate -> {
				IrisState s = stargate.stateOrNull(IrisState.state);

				if (s == null)
					return ActionResult.PASS;

				s.open = !s.open;

				stargate.markDirty();
				return ActionResult.SUCCESS;
			}).orElse(ActionResult.PASS);
		}

		ItemStack heldItem = player.getStackInHand(hand);
        BlockState newSetState = null;

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem)
            newSetState = blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, heldItem, hit));

        if (newSetState != null || heldItem.isEmpty()) {
            be.setBlockSet(newSetState);
            be.markDirty();

			return ActionResult.SUCCESS;
        }

		return ActionResult.PASS;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock()) && world.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity)
            blockEntity.onBreak(state);

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.getItem() instanceof StargateItem item && world.getBlockEntity(pos) instanceof StargateBlockEntity be)
            be.onPlacedWithKernel(world, pos, item.getCreator());

		super.onPlaced(world, pos, state, placer, stack);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StargateBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state,
																  @NotNull BlockEntityType<T> type) {
		return (world1, blockPos, blockState, ticker) -> {
			if (ticker instanceof StargateBlockEntity exterior)
				exterior.tick(world1, blockPos, blockState);
		};
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!(world.getBlockEntity(pos) instanceof StargateBlockEntity be) || !be.isLinked()) return;

		be.gate().ifPresent(stargate -> TEvents.handle(
				new StargateBlockTickEvent.Random(stargate, world, pos, state, random)
		));
	}
}
