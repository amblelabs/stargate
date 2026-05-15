package dev.amblelabs.stargate.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DHDBlockEntity(StargateBlockEntities.DHD, pos, state);
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof DHDBlockEntity blockEntity) {
            blockEntity.onPlace(blockState, level, blockPos, blockState2, bl);
        }
    }

    public static Properties defaultProps() {
        return Properties.of()
                .mapColor(MapColor.COLOR_GRAY);
    }
}
