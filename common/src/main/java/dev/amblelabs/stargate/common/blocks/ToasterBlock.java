package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ToasterBlock extends BaseEntityBlock {

    private static BlockPos[] buildPositions(Direction facing) {
        BlockPos zero = BlockPos.ZERO;

        return new BlockPos[] {
                zero.above(),
                zero.relative(facing.getClockWise()),
                zero.relative(facing.getCounterClockWise()),
                zero.below(),
                zero.above().relative(facing.getClockWise()),
                zero.above().relative(facing.getCounterClockWise()),
                zero.below().relative(facing.getClockWise()),
                zero.below().relative(facing.getCounterClockWise())
        };
    }

    public static final VoxelShape SHAPE = Block.box(5, 1, 5, 11, 7, 11);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final MapCodec<StargateBlock> CODEC = simpleCodec(StargateBlock::new);

    private static final BlockPos[] POSITIONS_NORTH = buildPositions(Direction.NORTH);
    private static final BlockPos[] POSITIONS_EAST = buildPositions(Direction.EAST);

    private static final Block[] requiredBlocks = {
            Blocks.IRON_BLOCK, // up
            Blocks.IRON_BLOCK, // east
            Blocks.IRON_BLOCK, // west
            StargateBlocks.BLOCK_OF_NAQUADAH, // down
            Blocks.CUT_COPPER_STAIRS, // upEast
            Blocks.CUT_COPPER_STAIRS, // upWest
            Blocks.CUT_COPPER_STAIRS, // downEast
            Blocks.CUT_COPPER_STAIRS // downWest
    };

    public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;

    public ToasterBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof ToasterBlockEntity toaster && !toaster.getHeldItem().isEmpty())
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), toaster.getHeldItem());

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof ToasterBlockEntity toaster) {
            Optional<RecipeHolder<ToastingRecipe>> optional = toaster.getToastableRecipe(stack);

            if (optional.isPresent()) {
                if (!level.isClientSide && toaster.placeFood(player, stack, optional.get().value().getCookingTime())) {
                    state.setValue(ACTIVE, true);

                    level.playSound(
                            null,
                            pos,
                            StargateSounds.TOASTER_LOAD,
                            SoundSource.BLOCKS,
                            1.0F,
                            1.5F
                    );

                    level.playSound(
                            null,
                            pos,
                            StargateSounds.TOASTER_ACTIVE,
                            SoundSource.BLOCKS
                    );
                }

                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Direction facing = state.getValue(FACING);

        BlockPos[] positions = facing == Direction.NORTH || facing == Direction.SOUTH
                ? POSITIONS_NORTH : POSITIONS_EAST;

        BlockPos[] finalPos = new BlockPos[positions.length];

        for (int i = 0; i < finalPos.length; i++) {
            BlockPos blockPos = pos.offset(positions[i]);
            finalPos[i] = blockPos;

            BlockState block = level.getBlockState(blockPos);

            if (block.getBlock() != requiredBlocks[i]) {
                StargateAPI.LOGGER.debug("Block {}@{} != {}", block.getBlock(), blockPos, requiredBlocks[i]);
                return InteractionResult.PASS;
            }
        }

        if (level instanceof ServerLevel serverLevel) {
            for (BlockPos clearPos : finalPos) {
                level.destroyBlock(clearPos, false);
            }

            // FIXME
            throw new IllegalStateException("Not implemented");
//            StargateItems.ORLIN_STARGATE.place(serverLevel, pos.below(), facing);
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return !level.isClientSide() && state.getValue(ACTIVE) ? createTickerHelper(blockEntityType, StargateBlockEntities.TOASTER, ToasterBlockEntity::cookTick) : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ToasterBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}