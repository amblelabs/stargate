package dev.amble.stargate.api;

import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ServerStargateNetwork extends StargateNetwork {
	static {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerStargateNetwork.getInstance().sync(List.of(handler.getPlayer()));
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			ServerStargateNetwork.instance = null;
		});
	}

	@Override
	public Optional<Stargate> remove(Address address) {
		Optional<Stargate> removed = super.remove(address);

		removed.ifPresent(stargate -> {
			this.sync();
		});

		return removed;
	}

	@Override
	public boolean add(Address address, Stargate stargate) {
		boolean success = super.add(address, stargate);

		if (success) {
			this.sync();
		}

		return success;
	}

	/**
	 * Syncs the network with all clients
	 */
	private void sync(Collection<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing {} stargates!", this.lookup.size());

		NbtCompound nbt = this.toNbt();
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);
		buf.writeBoolean(true);

		for (ServerPlayerEntity player : targets) {
			ServerPlayNetworking.send(player, PACKET, buf);
		}

		StargateServerData.get().markDirty();
	}
	private void sync() {
		if (ServerLifecycleHooks.get() == null) {
			Scheduler.get().runTaskLater(this::sync, TaskStage.END_SERVER_TICK, TimeUnit.TICKS, 1); // queue the sync when the server exists
			return;
		}

		this.sync(PlayerLookup.all(ServerLifecycleHooks.get()));
	}
	private void sync(Stargate gate, Collection<ServerPlayerEntity> targets) {
		StargateMod.LOGGER.debug("Syncing stargate {}", gate.getAddress());

		NbtCompound nbt = gate.toNbt(true);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeNbt(nbt);
		buf.writeBoolean(false);

		for (ServerPlayerEntity player : targets) {
			ServerPlayNetworking.send(player, PACKET, buf);
		}

		StargateServerData.get().markDirty();
	}
	public void sync(Stargate gate) {
		if (ServerLifecycleHooks.get() == null) {
			Scheduler.get().runTaskLater(() -> this.sync(gate), TaskStage.END_SERVER_TICK, TimeUnit.TICKS, 1); // queue the sync when the server exists
			return;
		}

		this.sync(gate, PlayerLookup.all(ServerLifecycleHooks.get()));
	}

	private static ServerStargateNetwork instance;

	public static ServerStargateNetwork getInstance() {
		if (instance == null) {
			StargateMod.LOGGER.info("Creating new SERVER PhoneBook instance");
			instance = new ServerStargateNetwork();
		}
		return instance;
	}
}
