package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class StargateBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final MapCodec<StargateBlock> CODEC = simpleCodec(StargateBlock::new);

    public StargateBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Stargate stargate;
        if (level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity && (stargate = blockEntity.stargate()) != null) {
            if (stack.isEmpty() && blockEntity.getBlockSet() != null) {
                blockEntity.setBlockSet(null);
                return ItemInteractionResult.SUCCESS;
            }

            if (!(stack.getItem() instanceof BlockItem blockItem))
                return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

            if (blockItem.getBlock().defaultBlockState() != blockEntity.getBlockSet()) {
                blockEntity.setBlockSet(blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, hand, stack, hitResult)));
                return ItemInteractionResult.SUCCESS;
            }

            StargateBlockEvents.notify(events -> events.stargate$useItem(stargate, blockEntity, stack, state, player, hand, hitResult));
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        Stargate stargate;
        if (level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity && (stargate = blockEntity.stargate()) != null)
            StargateBlockEvents.notify(events -> events.stargate$use(stargate, blockEntity, state, level, pos, player, hitResult));

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // always ServerLevel, actually.
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity) {
            Stargate stargate = blockEntity.stargate();

            if (stargate == null)
                return;

            StargateBlockEvents.notify(events -> events.stargate$place(
                    stargate, blockEntity, state, serverLevel, pos, oldState, movedByPiston));

            stargate.setChanged(); // forces sync
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Stargate stargate;

        // always ServerLevel, actually.
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity && (stargate = blockEntity.stargate()) != null) {
            StargateBlockEvents.notify(events -> events.stargate$break(stargate, blockEntity, state, serverLevel, pos, newState, movedByPiston));
            ServerStargateNetwork.get(level).remove(stargate.getId()); // TODO: make a behavior do this maybe

            blockEntity.setStargate(null);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Stargate stargate;
        if (level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity && (stargate = blockEntity.stargate()) != null)
            StargateBlockEvents.notify(events -> events.stargate$randomTick(stargate, state, level, pos, random));

        super.randomTick(state, level, pos, random);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new StargateBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BlockEntityHelper.createTicker(type, StargateBlockEntities.STARGATE, StargateBlockEntity::tick);
    }

    public static Properties defaultProps() {
        return Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(50f, 1200f)
                .noOcclusion()
                .dynamicShape()
                .lightLevel(state -> 8);
    }
}
