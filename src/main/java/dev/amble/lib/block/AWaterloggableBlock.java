package dev.amble.lib.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.Nullable;

public class AWaterloggableBlock extends ABlock implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    @SafeVarargs
    public AWaterloggableBlock(Settings settings, BlockBehavior<? extends AWaterloggableBlock, ?>... behaviors) {
        super(settings, behaviors);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState state = this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

        return BlockPlacementBehavior.IDX.get(this).getPlacementState(state, ctx);
    }
}
