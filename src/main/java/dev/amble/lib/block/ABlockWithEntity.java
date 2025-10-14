package dev.amble.lib.block;

import dev.amble.lib.blockentity.ABlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class ABlockWithEntity<T extends ABlockEntity> extends BlockWithEntity {

    protected ABlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return super.mirror(state, mirror);
    }

    @Override
    public abstract @Nullable T createBlockEntity(BlockPos pos, BlockState state);
}
