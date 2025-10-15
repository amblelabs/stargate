package dev.amble.stargate.block;

import dev.amble.lib.block.ABlock;
import dev.amble.lib.block.behavior.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.item.StargateLinkableItem;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DHDBlock extends ABlock implements BlockEntityProvider {

	public static final VoxelShape DHDSHAPE = Block.createCuboidShape(4.0F, 0.0F, 4.0F, 12.0F, 14.0F, 12.0F);

	public DHDBlock(Settings settings) {
		super(settings, HorizontalBlockBehavior.withPlacement(true),
				BlockWithEntityBehavior.Ticking.withInvisibleModel(DHDBlockEntity::new));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return DHDSHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		// FIXME: looks silly.
		if (!(player.getStackInHand(hand).getItem() instanceof StargateLinkableItem) && world.getBlockEntity(pos) instanceof DHDBlockEntity be && hand == Hand.MAIN_HAND) {
			return be.onUse(state, world, pos, player);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}
}
