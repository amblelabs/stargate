package dev.amble.stargate.api.network;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v2.ClientStargate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ClientStargateNetwork extends StargateNetwork<ClientStargate>
		implements ClientPlayConnectionEvents.Disconnect, ClientTickEvents.StartTick {

	private ClientStargateNetwork() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC, this::onSyncPartial);
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ALL, this::onSyncAll);

		ClientTickEvents.START_CLIENT_TICK.register(this);
		ClientPlayConnectionEvents.DISCONNECT.register(this);
	}

	@Override
	public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
		this.reset();
	}

	@Override
	public void onStartTick(MinecraftClient client) {
		if (client.world == null) return;
		Identifier worldId = client.world.getRegistryKey().getValue();

		this.lookup.values().stream()
				.filter(stargate -> stargate.address().pos().getDimension().getValue().equals(worldId))
				.forEach(ClientStargate::tick);
	}

	@Override
	protected ClientStargate fromNbt(NbtCompound nbt) {
		return new ClientStargate(nbt);
	}

	private void onSyncPartial(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		NbtCompound nbt = buf.readNbt();
		ClientStargate gate = this.fromNbt(nbt);

		ClientStargate existing = this.lookup.put(gate.address(), gate);

		if (existing != null) existing.age();

		StargateMod.LOGGER.info("Received stargate {}", gate.address());
	}

	private void onSyncAll(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		NbtCompound nbt = buf.readNbt();
		this.fromNbt(nbt, true);

		StargateMod.LOGGER.debug("Received {} stargates!", this.lookup.size());
	}

	private void reset() {
		this.lookup.clear();
	}

	private static ClientStargateNetwork instance;

	public static ClientStargateNetwork get() {
		return instance == null ? instance = new ClientStargateNetwork() : instance;
	}
}
