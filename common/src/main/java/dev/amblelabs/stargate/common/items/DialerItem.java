package dev.amblelabs.stargate.common.items;

import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
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

		if (!stack.has(StargateComponents.STARGATE)) {
			stack.set(StargateComponents.STARGATE, new StargateLinkedComponent(stargate.getId()));
			return InteractionResult.SUCCESS;
		}

		Stargate target = getStargate(context.getLevel(), context.getItemInHand());
		if (target == null) return InteractionResult.FAIL;

        GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

        if (closed != null) {
            closed.address = target.state(C7State.state).address();
			stargate.setChanged();
        }

		stack.consume(1, context.getPlayer());
        return InteractionResult.SUCCESS;
    }

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		tooltipComponents.add(Component.translatable("tooltip.stargate.dialer.hint")
				.withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));

		this.handleTooltip(stack, tooltipComponents);
	}

	@Environment(EnvType.CLIENT)
	private void handleTooltip(ItemStack stack, List<Component> tooltip) {
		UUID id = getStargateAddressFromKey(stack, "Address");

		if (id == null)
			return;

		if (!Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("tooltip.stargate.link_item.holdformoreinfo")
					.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			return;
		}

		ClientLevel level = Minecraft.getInstance().level;
		Stargate stargate = getStargate(level, id);

		if (stargate != null) {
			tooltip.add(Component.translatable("text.stargate.gate").append(Component.literal(": "))
					.withStyle(ChatFormatting.BLUE));

			tooltip.add(Component.literal("> " + stargate.getId())
					.withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	public static @Nullable Stargate getStargate(Level world, ItemStack stack) {
		return getStargateFromKey(world, stack, "Address");
	}

	public static @Nullable UUID getStargateAddressFromKey(ItemStack stack, String path) {
		StargateLinkedComponent linked = stack.get(StargateComponents.STARGATE);
		return linked == null ? null : linked.stargate();
	}

	public static @Nullable Stargate getStargateFromKey(Level world, ItemStack stack, String path) {
		UUID id = getStargateAddressFromKey(stack, path);
		if (id == null) return null;

		return getStargate(world, id);
	}

	public static @Nullable Stargate getStargate(Level world, UUID address) {
		if (world instanceof ServerLevel)
			return ServerStargateNetwork.GLOBAL.get(address);

		return StargateNetwork.get(world).get(address);
	}
}