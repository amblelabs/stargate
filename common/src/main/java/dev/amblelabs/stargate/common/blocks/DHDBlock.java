package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class DHDBlock extends BaseEntityBlock {

    public static final MapCodec<DHDBlock> CODEC = simpleCodec(DHDBlock::new);

    public DHDBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DHDBlockEntity(pos, state);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(blockPos) instanceof DHDBlockEntity blockEntity)
            blockEntity.onPlace(blockState, serverLevel, blockPos, blockState2, bl);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof DHDBlockEntity dhdBlockEntity)
            dhdBlockEntity.onBlockBreak(serverLevel);

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return BlockEntityHelper.createTicker(type, StargateBlockEntities.DHD, DHDBlockEntity::tick);
    }

    public static Properties defaultProps() {
        return Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .dynamicShape()
                .noOcclusion()
                .lightLevel(state -> 3);
    }
}
