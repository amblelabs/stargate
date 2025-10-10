package dev.amble.stargate.api;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v3.Stargate;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StargateServerData extends PersistentState {

	// C8 addresses: 6 id numbers + 1 self dim char
	// C9 addresses: 6 id numbers + 1 self dim char + 1 target dim char
	public @Nullable Stargate getLocal(long address) {
		address = AddressProvider.Local.getId(address);
	}

	// 9C addresses: 8 id characters + 1 target dim char
	public @Nullable Stargate getGlobal(long address) {
		address = AddressProvider.Local.getId(address);
	}

	public @Nullable void removeGlobal(long address) {

	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("Network", ServerStargateNetwork.get().toNbt(false));

		return nbt;
	}

	public static StargateServerData loadNbt(NbtCompound nbt) {
		StargateServerData data = new StargateServerData();

		if (nbt.contains("Network")) {
			ServerStargateNetwork.get().fromNbt(nbt.getCompound("Network"), true);
		}

		return data;
	}

	public static StargateServerData get(ServerWorld world) {
		PersistentStateManager manager = world.getPersistentStateManager();
		StargateServerData state = manager.getOrCreate(StargateServerData::loadNbt, StargateServerData::new, StargateMod.MOD_ID);

		state.markDirty();
		return state;
	}

	private static StargateServerData get(MinecraftServer server) {
		return get(server.getWorld(World.OVERWORLD));
	}

	public static StargateServerData get() {
		return get(ServerLifecycleHooks.get());
	}

	public static void init() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> get(server).markDirty());
	}
}
