package dev.amble.stargate.api.network;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.Stargate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientStargateNetwork extends StargateNetwork<Stargate>
		implements ClientPlayConnectionEvents.Disconnect, ClientTickEvents.StartTick {

	private ClientStargateNetwork() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC, this::onSyncPartial);
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ALL, this::onSyncAll);
		ClientPlayNetworking.registerGlobalReceiver(REMOVE, this::onRemove);

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

		for (Stargate stargate : this.lookup.values()) {
			if (stargate.address().pos().getDimension().getValue().equals(worldId))
				stargate.tick();
		}
	}

	@Override
	protected Stargate fromNbt(NbtCompound nbt) {
		return Stargate.fromNbt(nbt, true);
	}

	private void onRemove(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		UUID uuid = buf.readUuid();
		String addr = buf.readString();

		this.lookup.remove(uuid, addr);
	}

	private void onSyncPartial(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		NbtCompound nbt = buf.readNbt();
		Stargate gate = this.fromNbt(nbt);

		Stargate existing = this.lookup.put(gate);

		if (existing != null)
			existing.age();

		StargateMod.LOGGER.debug("Received stargate {}", gate.address());
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
