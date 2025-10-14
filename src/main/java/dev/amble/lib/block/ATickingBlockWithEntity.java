package dev.amble.lib.block;

import dev.amble.lib.blockentity.ATickingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class ATickingBlockWithEntity<T extends ATickingBlockEntity> extends ABlockWithEntity<T> implements TickingShit<T> {

    protected ATickingBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable T createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public BlockEntityType<T> getBlockEntityType() {
        return null;
    }
}
