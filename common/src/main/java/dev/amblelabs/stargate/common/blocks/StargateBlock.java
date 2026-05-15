package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class StargateBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity) {
            TEvents.handle(new StargateBlockEvents.UseItem(blockEntity.stargate, blockEntity, stack, state, player, hand, hitResult));
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity) {
            TEvents.handle(new StargateBlockEvents.Use(blockEntity.stargate, blockEntity, state, player, hitResult));
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof StargateBlockEntity blockEntity) {
            // FIXME: this wont work properly in multiplayer, client code must handle the PrototypeIdentityState and compensate.
            PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().orElseThrow().value();

            entry.make(StargateEcs.States, blockEntity.stargate, NbtDeserializer.Context.fromLevel(level));
            blockEntity.setChanged(); // TODO: figure out if this is even needed
        }
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
        return createTickerHelper(type, StargateBlockEntities.STARGATE,
                (level1, pos, state1, be) -> be.tick(level1, pos, state1, be));
    }

    public static Properties defaultProps() {
        return Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .strength(50f, 1200f)
                .noOcclusion()
                .dynamicShape()
                .noCollission()
                .lightLevel(state -> 8);
    }
}
