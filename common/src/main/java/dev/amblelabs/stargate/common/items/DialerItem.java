package dev.amblelabs.stargate.common.items;

import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.common.I18n;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.behavior.GateManagerBehavior;
import dev.amblelabs.stargate.common.impl.ecs.state.C7State;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.items.component.StargateLinkedComponent;
import dev.amblelabs.stargate.common.lib.StargateComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * right click to link to stargate
 * right click on stargate to dial
 */
public class DialerItem extends Item {

	public static final String TAG_ADDRESS = "Address";

	public DialerItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof StargateBlockEntity stargateBlock))
            return super.useOn(context);

        Stargate stargate = stargateBlock.stargate();
        if (stargate == null) return super.useOn(context);

		ItemStack stack = context.getItemInHand();

		if (!stack.has(StargateComponents.STARGATE.get())) {
			stack.set(StargateComponents.STARGATE.get(), new StargateLinkedComponent(stargate.getId()));
			return InteractionResult.SUCCESS;
		}

		Stargate target = getStargate(context.getLevel(), context.getItemInHand());
		if (target == null) return InteractionResult.FAIL;

        GateState<?> state = GateManagerBehavior.get().get(stargate);

        if (state instanceof GateState.Closed closed) {
            closed.address = target.state(C7State.state).address();
			stargate.setChanged();
        } else if (context.getPlayer() != null) {
			context.getPlayer().sendSystemMessage(I18n.Items.DIALER_FAIL);
		}

		stack.consume(1, context.getPlayer());
        return InteractionResult.SUCCESS;
    }

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		tooltipComponents.add(I18n.Items.DIALER_TOOLTIP.copy()
				.withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

		this.handleTooltip(stack, tooltipComponents);
	}

	@Environment(EnvType.CLIENT)
	private void handleTooltip(ItemStack stack, List<Component> tooltip) {
		UUID id = getStargateAddressFromKey(stack);

		if (id == null)
			return;

		if (!Screen.hasShiftDown()) {
			tooltip.add(I18n.Items.GENERIC_ITEM_SHIFT_TOOLTIP.copy()
					.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			return;
		}

		ClientLevel level = Minecraft.getInstance().level;
		Stargate stargate = getStargate(level, id);

		if (stargate != null) {
			tooltip.add(I18n.Items.DIALER_TOOLTIP_HEADER.copy().withStyle(ChatFormatting.BLUE));
			tooltip.add(I18n.Items.dialerDetails(stargate).withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	public static @Nullable Stargate getStargate(Level world, ItemStack stack) {
		return getStargateFromKey(world, stack);
	}

	public static @Nullable UUID getStargateAddressFromKey(ItemStack stack) {
		StargateLinkedComponent linked = stack.get(StargateComponents.STARGATE.get());
		return linked == null ? null : linked.stargate();
	}

	public static @Nullable Stargate getStargateFromKey(Level world, ItemStack stack) {
		UUID id = getStargateAddressFromKey(stack);
		if (id == null) return null;

		return getStargate(world, id);
	}

	public static @Nullable Stargate getStargate(Level world, UUID address) {
		if (world instanceof ServerLevel)
			return ServerStargateNetwork.GLOBAL.get(address);

		return StargateNetwork.get(world).get(address);
	}
}