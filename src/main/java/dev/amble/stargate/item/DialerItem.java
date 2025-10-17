package dev.amble.stargate.item;

import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.data.StargateLinkable;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.address.GlobalAddressState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * right click to link to stargate
 * right click on stargate to dial
 */
public class DialerItem extends StargateLinkableItem {

	public DialerItem(Settings settings) {
		super(settings, true);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().isClient()) return super.useOnBlock(context);

		ServerWorld world = (ServerWorld) context.getWorld();
		BlockPos pos = context.getBlockPos();
		ItemStack hand = context.getStack();

		if (world.getBlockEntity(pos) instanceof StargateLinkable be) {
			Stargate gate = be.asGate();

			if (gate == null) return ActionResult.FAIL;

			if (!isLinked(hand)) {
				this.link(hand, gate);
				return ActionResult.SUCCESS;
			}

			Stargate target = StargateLinkableItem.getStargate(world, hand);
			if (target == null) return ActionResult.FAIL;

			GateState.Closed closed = gate.stateOrNull(GateState.Closed.state);

			if (closed != null) {
				closed.address = AddressProvider.Global.asString(target.globalAddress());
				gate.markDirty();
			}

			hand.decrement(1);
			return ActionResult.SUCCESS;
		}

		return super.useOnBlock(context);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("tooltip.stargate.dialer.hint").formatted(Formatting.DARK_PURPLE)
				.formatted(Formatting.ITALIC));

		super.appendTooltip(stack, world, tooltip, context);
	}
}
