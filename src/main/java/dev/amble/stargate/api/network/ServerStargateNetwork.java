package dev.amble.stargate.api.network;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.v3.Stargate;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;
import java.util.stream.Stream;

public class ServerStargateNetwork extends StargateNetwork<Stargate>
		implements ServerPlayConnectionEvents.Join, ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {

	private static volatile ServerStargateNetwork instance;

	private ServerStargateNetwork() {
		ServerPlayConnectionEvents.JOIN.register(this);
		ServerLifecycleEvents.SERVER_STOPPED.register(this);
		ServerTickEvents.END_SERVER_TICK.register(this);
	}

	@Override
	protected void postProcessNbt(NbtList list, Stargate[] gates) {
		for (int i = 0; i < gates.length; i++) {
			Stargate gate = gates[i];

			if (gate == null)
				continue;

			throw new IllegalStateException("todo");
		}

		super.postProcessNbt(list, gates);
	}

	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		ServerStargateNetwork.get().syncAll(Stream.of(handler.getPlayer()));
	}

	@Override
	protected Stargate fromNbt(NbtCompound nbt) {
		throw new IllegalStateException("todo");
	}

	@Override
	public Optional<Stargate> remove(Address address) {
		Optional<Stargate> removed = super.remove(address);
		removed.ifPresent(s -> this.removePartial(address));

		StargateServerData.get().markDirty();
		return removed;
	}

	@Override
	public boolean add(Stargate stargate) {
		boolean success = super.add(stargate);
		if (success) this.syncPartial(stargate);

		StargateServerData.get().markDirty();
		return success;
	}

	/**
	 * Syncs the network with all clients
	 */
	private void syncAll(Stream<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing {} stargates!", this.lookup.size());

		NbtCompound nbt = this.toNbt(true);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);

		targets.forEach(player ->
				ServerPlayNetworking.send(player, SYNC_ALL, buf));

		StargateServerData.get().markDirty();
	}

	private void syncPartial(Stargate gate, Stream<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing stargate {}", gate.address());

		NbtCompound nbt = new NbtCompound();
		gate.toNbt(nbt, true);

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(gate.address().id());
		buf.writeNbt(nbt);

		targets.forEach(player ->
				ServerPlayNetworking.send(player, SYNC, buf));

		StargateServerData.get().markDirty();
	}

	private void removePartial(Address address, Stream<ServerPlayerEntity> targets) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(address.id());
		buf.writeString(address.text());

		targets.forEach(player ->
				ServerPlayNetworking.send(player, REMOVE, buf));

		StargateServerData.get().markDirty();
	}

	private void removePartial(Address address) {
		this.removePartial(address, PlayerLookup.all(ServerLifecycleHooks.get()).stream());
	}

	public void syncPartial(Stargate gate) {
		// TODO: null server handling
		this.syncPartial(gate, PlayerLookup.all(ServerLifecycleHooks.get()).stream());
	}

	public static ServerStargateNetwork get() {
		return instance == null ? instance = new ServerStargateNetwork() : instance;
	}

	@Override
	public void onServerStopped(MinecraftServer server) {
		instance = null;
	}

	@Override
	public void onEndTick(MinecraftServer server) {
		for (Stargate stargate : this.lookup.values()) {
			stargate.tick();

			if (stargate.dirty()) {
				this.syncPartial(stargate);
				stargate.unmarkDirty();
			}
		}
	}
}
