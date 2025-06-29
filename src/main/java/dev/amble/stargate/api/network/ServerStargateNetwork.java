package dev.amble.stargate.api.network;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.v2.ServerStargate;
import dev.amble.stargate.api.v2.Stargate;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;
import java.util.stream.Stream;

public class ServerStargateNetwork extends StargateNetwork<ServerStargate>
		implements ServerPlayConnectionEvents.Join, ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {

	private ServerStargateNetwork() {
		ServerPlayConnectionEvents.JOIN.register(this);
		ServerLifecycleEvents.SERVER_STOPPED.register(this);
		ServerTickEvents.END_SERVER_TICK.register(this);
	}

	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		ServerStargateNetwork.get().syncAll(Stream.of(handler.getPlayer()));
	}

	@Override
	protected ServerStargate fromNbt(NbtCompound nbt) {
		return new ServerStargate(nbt);
	}

	@Override
	public Optional<Stargate> remove(Address address) {
		Optional<Stargate> removed = super.remove(address);
		removed.ifPresent(s -> this.syncAll());

		StargateServerData.get().markDirty();;
		return removed;
	}

	@Override
	protected boolean add(Address address, ServerStargate stargate) {
		boolean success = super.add(address, stargate);
		if (success) this.syncAll();

		StargateServerData.get().markDirty();
		return success;
	}

	/**
	 * Syncs the network with all clients
	 */
	private void syncAll(Stream<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing {} stargates!", this.lookup.size());

		NbtCompound nbt = this.toNbt();
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);

		targets.forEach(player ->
				ServerPlayNetworking.send(player, SYNC_ALL, buf));

		StargateServerData.get().markDirty();
	}

	private void syncAll() {
		// TODO: null server handling
		this.syncAll(PlayerLookup.all(ServerLifecycleHooks.get()).stream());
	}

	private void syncPartial(ServerStargate gate, Stream<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing stargate {}", gate.address());

		NbtCompound nbt = gate.toNbt(true);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);

		targets.forEach(player ->
				ServerPlayNetworking.send(player, SYNC, buf));

		StargateServerData.get().markDirty();
	}

	public void syncPartial(ServerStargate gate) {
		// TODO: null server handling
		this.syncPartial(gate, PlayerLookup.all(ServerLifecycleHooks.get()).stream());
	}

	private static ServerStargateNetwork instance;

	public static ServerStargateNetwork get() {
		return instance == null ? instance = new ServerStargateNetwork() : instance;
	}

	@Override
	public void onServerStopped(MinecraftServer server) {
		instance = null;
	}

	@Override
	public void onEndTick(MinecraftServer server) {
		for (ServerStargate stargate : this.lookup.values()) {
			stargate.tick();
		}
	}
}
