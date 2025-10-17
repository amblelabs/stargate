package dev.amble.stargate.block;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.AWaterloggableBlock;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.util.NonNull;
import dev.amble.stargate.api.gates.event.block.StargateBlockTickEvent;
import dev.amble.stargate.api.gates.state.iris.IrisState;
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

@SuppressWarnings("deprecation")
public class StargateBlock extends AWaterloggableBlock implements BlockEntityProvider {

	public StargateBlock(ABlockSettings settings) {
		super(settings,
				BlockWithEntityBehavior.Ticking.withInvisibleModel(StargateBlockEntity::new),
				HorizontalBlockBehavior.withPlacement(true)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND || !(world.getBlockEntity(pos) instanceof StargateBlockEntity be) || !be.isLinked())
            return ActionResult.PASS;

		ItemStack heldItem = player.getStackInHand(hand);

		if (!player.isSneaking()) {
			if (!heldItem.isEmpty()) return ActionResult.PASS;
			if (world.isClient()) return ActionResult.SUCCESS;

			// TODO: silly, move this to a result event
			return NonNull.get(be.applyGate(stargate -> {
				IrisState s = stargate.stateOrNull(IrisState.state);

				if (s == null)
					return ActionResult.PASS;

				s.open = !s.open;

				stargate.markDirty();
				return ActionResult.SUCCESS;
			}), ActionResult.PASS);
		}

        BlockState newSetState = null;

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem blockItem)
            newSetState = blockItem.getBlock().getPlacementState(new ItemPlacementContext(player, hand, heldItem, hit));

        if (newSetState != null || heldItem.isEmpty()) {
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
