package dev.amble.stargate.block;

import dev.amble.lib.block.ABlock;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class DHDBlock extends ABlock implements BlockEntityProvider {

	public static final VoxelShape DHDSHAPE = Block.createCuboidShape(4.0F, 0.0F, 4.0F, 12.0F, 14.0F, 12.0F);

	public DHDBlock(ABlockSettings settings) {
		super(settings, HorizontalBlockBehavior.withPlacement(true),
				BlockWithEntityBehavior.Ticking.withInvisibleModel(DHDBlockEntity::new));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return DHDSHAPE;
	}
}
