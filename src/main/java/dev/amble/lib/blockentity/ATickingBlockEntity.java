package dev.amble.lib.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ATickingBlockEntity extends ABlockEntity {

    public ATickingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static <E extends BlockEntity> void tick(World world, BlockPos blockPos, BlockState blockState, E e) {
        ((ATickingBlockEntity) e).tick(world, blockPos, blockState);
    }

    protected abstract void tick(World world, BlockPos blockPos, BlockState blockState);
}
