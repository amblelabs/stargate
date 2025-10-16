package dev.amble.stargate.item;

import dev.amble.stargate.api.StargateClientData;
import dev.amble.stargate.api.StargateData;
import dev.amble.stargate.api.WorldUtil;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.address.GlobalAddressState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class StargateLinkableItem extends Item {
	private final boolean showTooltip;

	public StargateLinkableItem(Settings settings, boolean showTooltip) {
		super(settings);
		this.showTooltip = showTooltip;
	}

	public void link(ItemStack stack, Stargate gate) {
		this.link(stack, gate.resolve(GlobalAddressState.state).address());
	}

	public void link(ItemStack stack, long address) {
		stack.getOrCreateNbt().putLong("Address", address);
	}

	public static boolean isLinked(ItemStack stack) {
		return stack.getOrCreateNbt().contains("Address");
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		this.handleTooltip(stack, tooltip);
		super.appendTooltip(stack, world, tooltip, context);
	}

	private void handleTooltip(ItemStack stack, List<Text> tooltip) {
		if (!showTooltip)
			return;

		long id = StargateLinkableItem.getStargateAddressFromKey(stack, "Address");

		if (id == -1)
			return;

		if (!Screen.hasShiftDown()) {
			tooltip.add(Text.translatable("tooltip.stargate.link_item.holdformoreinfo").formatted(Formatting.GRAY)
					.formatted(Formatting.ITALIC));
			return;
		}

		Stargate stargate = StargateClientData.get().getGlobal(id);

		if (stargate != null) {
			tooltip.add(Text.translatable("text.stargate.gate").append(Text.literal(": ")).formatted(Formatting.BLUE));

			RegistryKey<World> dim = stargate.dimension();
			BlockPos pos = stargate.pos();
			Direction direction = stargate.facing();

			tooltip.add(Text.literal("> ").append(stargate.stateOrNull(GlobalAddressState.state) + "")
					.formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("> ").append(WorldUtil.worldText(dim)).formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("> " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
					.formatted(Formatting.DARK_GRAY));
			tooltip.add(Text.literal("> " + direction).formatted(Formatting.DARK_GRAY));
		}
	}

	public static boolean isOf(World world, ItemStack stack, Stargate tardis) {
		return StargateLinkableItem.getStargate(world, stack) == tardis;
	}

	public static Stargate getStargate(World world, ItemStack stack) {
		return StargateLinkableItem.getStargateFromKey(world, stack, "Address");
	}

	public static long getStargateAddressFromKey(ItemStack stack, String path) {
		NbtCompound nbt = stack.getOrCreateNbt();
		NbtElement element = nbt.get(path);

		if (element == null)
			return -1;

		return nbt.getLong(path);
	}

	public static Stargate getStargateFromKey(World world, ItemStack stack, String path) {
		return StargateLinkableItem.getStargate(world, StargateLinkableItem.getStargateAddressFromKey(stack, path));
	}

	public static Stargate getStargate(World world, long address) {
		return StargateData.apply(world, data -> data.getGlobal(address));
	}
}
