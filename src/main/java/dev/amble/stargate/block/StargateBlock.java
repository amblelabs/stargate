package dev.amble.stargate.block;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.AWaterloggableBlock;
import dev.amble.lib.block.behavior.api.BlockBehaviorLike;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.lib.blockentity.ABlockEntity;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.block.StargateBlockUseEvent;
import dev.amble.stargate.api.event.block.StargateBlockTickEvent;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.BiFunction;

// uses unstable amblekit feat/ports block behavior api
@SuppressWarnings("UnstableApiUsage")
public class StargateBlock extends AWaterloggableBlock implements BlockEntityProvider {

	public StargateBlock(ABlockSettings settings) {
		super(settings,
				BlockWithEntityBehavior.Ticking.withInvisibleModel(StargateBlockEntity::new),
				HorizontalBlockBehavior.withPlacement(true)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND || !(world.getBlockEntity(pos) instanceof StargateBlockEntity be))
            return ActionResult.PASS;

		Stargate stargate = be.asGate();

		if (stargate == null)
			return ActionResult.PASS;

		boolean success = TEvents.handle(new StargateBlockUseEvent(stargate, be, player, world, state, pos, hand, hit));

		if (success)
			return ActionResult.SUCCESS;

		if (!player.isSneaking() || world.isClient()) return ActionResult.PASS;

		ItemStack heldItem = player.getStackInHand(hand);
        BlockState newSetState = null;

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem)
            newSetState = blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, heldItem, hit));

        if (newSetState != null) {
            be.setBlockSet(newSetState);
            be.markDirty();

			return ActionResult.SUCCESS;
        }

		return ActionResult.PASS;
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!(world.getBlockEntity(pos) instanceof StargateBlockEntity be) || !be.isLinked()) return;

		be.acceptGate(stargate -> TEvents.handle(
				new StargateBlockTickEvent.Random(stargate, world, pos, state, random)
		));
	}
}
