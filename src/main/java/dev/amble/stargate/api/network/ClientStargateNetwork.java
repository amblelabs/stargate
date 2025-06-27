package dev.amble.stargate.api.network;

import dev.amble.stargate.StargateMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ClientStargateNetwork extends StargateNetwork<ClientStargate> implements ClientPlayConnectionEvents.Disconnect {

	private ClientStargateNetwork() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC, this::onSyncPartial);
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ALL, this::onSyncAll);

		ClientPlayConnectionEvents.DISCONNECT.register(this);
	}

	@Override
	public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
		this.reset();
	}

	@Override
	protected ClientStargate fromNbt(NbtCompound nbt) {
		return new ClientStargate(nbt);
	}

	private void onSyncPartial(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		NbtCompound nbt = buf.readNbt();
		ClientStargate gate = this.fromNbt(nbt);

		ClientStargate existing = this.lookup.put(gate.getAddress(), gate);

		if (existing != null) existing.age();

		StargateMod.LOGGER.debug("Received stargate {}", gate.getAddress());
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
