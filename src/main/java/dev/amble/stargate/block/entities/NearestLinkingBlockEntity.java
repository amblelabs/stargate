package dev.amble.stargate.block.entities;

import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.Stargate;
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

	public abstract boolean preventLinkingToStargate(Stargate gate);

	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.isClient()) return;

		Stargate nearest = ServerStargateNetwork.get().getNearTo(GlobalPos.create(world.getRegistryKey(), pos), 64).orElse(null);
		if (nearest == null) return;

		StargateRef ref = new StargateRef(nearest);

		if (this.preventLinkingToStargate(ref.get())) return;

		this.setStargate(ref);

		if (sendLinkMessage && placer instanceof ServerPlayerEntity player) {
			// FIXME: use translations
			player.sendMessage(Text.literal("Linked to " + nearest.address().text()), true);
		}
	}
}
