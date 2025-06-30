package dev.amble.stargate.block;

import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.OrlinGateKernel;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.amble.stargate.item.StargateItem;
import dev.amble.stargate.item.StargateLinkableItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StargateBlock extends HorizontalFacingBlock implements BlockEntityProvider, Waterloggable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty IRIS = Properties.LIT;
	public StargateBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false).with(IRIS, false));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
		return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING).add(WATERLOGGED).add(IRIS);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			if (!be.hasStargate()) return super.getOutlineShape(state, world, pos, context);
			Stargate gate = be.gate().get();
			if (gate.kernel() instanceof OrlinGateKernel) {
				return rotateShape(Direction.SOUTH, state.get(FACING), makeOrlinShape());
			}
		}
		return super.getOutlineShape(state, world, pos, context);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			if (!be.hasStargate()) return  super.getCollisionShape(state, world, pos, context);
			Stargate gate = be.gate().get();
			if (gate.kernel() instanceof OrlinGateKernel) {
				return rotateShape(Direction.SOUTH, state.get(FACING), makeOrlinShape());
			}
		}
		return super.getCollisionShape(state, world, pos, context);
	}

	public VoxelShape makeOrlinShape(){
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0F, 0.0F, 0.0F, 16.0F / 16, 8.0F / 16f, 16.0F / 16f), BooleanBiFunction.OR);

		return shape;
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(buffer[1],
					VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanBiFunction.OR));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!(player.getStackInHand(hand).getItem() instanceof StargateLinkableItem) && world.getBlockEntity(pos) instanceof StargateBlockEntity be && hand == Hand.MAIN_HAND) {
			return be.onUse(state, world, pos, player);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onEntityCollision(state, world, pos, entity);
		}

		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onEntityCollision(state, world, pos, entity);
		}

		super.onSteppedOn(world, pos, state, entity);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		if (!world.isClient()) {
			boolean bl = state.get(IRIS);
			if (bl != world.isReceivingRedstonePower(pos)) {
				if (bl) {
					world.scheduleBlockTick(pos, this, 4);
				} else {
					world.setBlockState(pos, state.cycle(IRIS), 2);
				}
			}

		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(IRIS) && !world.isReceivingRedstonePower(pos))
			world.setBlockState(pos, state.cycle(IRIS), 2);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}


	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof StargateBlockEntity) {
				((StargateBlockEntity) be).onBreak();
			}
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof StargateItem stargateItem)) return;
		if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
			be.onPlacedWithKernel(world, pos, stargateItem.getCreator());
		}

		super.onPlaced(world, pos, state, placer, itemStack);
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
				exterior.tick(world, blockPos, blockState, exterior);
		};
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		if (!(world.getBlockEntity(pos) instanceof StargateBlockEntity be)) return;
		if (!be.hasStargate()) return;
		if (!(be.gate().get().state() instanceof GateState.Open)) return;

		if (random.nextInt(100) < 5) {
			world.playSound(null, pos, StargateSounds.WORMHOLE_LOOP, SoundCategory.BLOCKS, 1.0f, 1.0f);
		}
	}
}
