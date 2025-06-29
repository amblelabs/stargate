package dev.amble.stargate.core.block.entities;

import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.network.StargateRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * A stargate block entity that can link to the nearest stargate.
 */
public abstract class NearestLinkingBlockEntity extends StargateLinkableBlockEntity {
	private final boolean sendLinkMessage;

	public NearestLinkingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean sendLinkMessage) {
		super(type, pos, state);
		this.sendLinkMessage = sendLinkMessage;
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;

		Stargate nearest = ServerStargateNetwork.get().getNearTo(GlobalPos.create(world.getRegistryKey(), pos), 64).orElse(null);
		if (nearest == null) return;

		this.setStargate(StargateRef.createAs(world, nearest));

		if (sendLinkMessage && placer instanceof ServerPlayerEntity player) {
			// FIXME: use translations
			player.sendMessage(Text.literal("Linked to " + nearest.address().text()), true);
		}
	}
}
