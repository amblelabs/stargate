package dev.amble.stargate.block.entities;

import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A stargate block entity that can link to the nearest stargate.
 */
public abstract class NearestLinkingBlockEntity extends StargateLinkableBlockEntity {

	private static final int[] OFFSETS = new int[] { 0, -1, 1 };

	private final boolean sendLinkMessage;

	public NearestLinkingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean sendLinkMessage) {
		super(type, pos, state);
		this.sendLinkMessage = sendLinkMessage;
	}

	public static @Nullable Stargate findNearest(ServerWorld world, BlockPos pos) {
		StargateServerData data = StargateServerData.get(world);

		if (data == null)
			return null;

		ChunkPos chunkPos = new ChunkPos(pos);

        for (int offsetX : OFFSETS) {
            for (int offsetZ : OFFSETS) {
                Collection<Stargate> collection = data.findByChunk(chunkPos.x + offsetX, chunkPos.z + offsetZ);

                if (collection == null || collection.isEmpty())
                    continue;

                return collection.iterator().next();
            }
        }

		return null;
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!(world instanceof ServerWorld serverWorld)) return;

		Stargate nearest = findNearest(serverWorld, pos);

		if (nearest == null) return;

		this.link(nearest);

		// FIXME: use translations
		if (sendLinkMessage && placer instanceof ServerPlayerEntity player)
            player.sendMessage(Text.literal("Linked to G:" + AddressProvider.Global.asString(address)), true);
	}
}
