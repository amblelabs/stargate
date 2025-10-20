package dev.amble.stargate.block;

import dev.amble.lib.api.ICantBreak;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.AWaterloggableBlock;
import dev.amble.lib.block.behavior.InvisibleBlockBehavior;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class StargateRingBlock extends AWaterloggableBlock implements BlockEntityProvider, ICantBreak {

	public StargateRingBlock(ABlockSettings settings) {
		super(settings,
				new InvisibleBlockBehavior(), new BlockWithEntityBehavior(StargateRingBlockEntity::new)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof StargateRingBlockEntity ringBlockEntity) {
			ItemStack heldItem = player.getStackInHand(hand);
			if (!heldItem.isEmpty()) {
				if (heldItem.getItem() instanceof BlockItem blockItem) {
					if (blockItem.getBlock().getDefaultState() != ringBlockEntity.getBlockSet()) {
						ringBlockEntity.setBlockSet(blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getMainHandStack(), hit)));
						ringBlockEntity.markDirty();
						return ActionResult.SUCCESS;
					}
					return ActionResult.FAIL;
				}
			} else {
				ringBlockEntity.setBlockSet(null);
				ringBlockEntity.markDirty();
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
