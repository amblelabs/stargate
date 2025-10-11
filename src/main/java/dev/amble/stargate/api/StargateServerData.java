package dev.amble.stargate.api;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v3.Stargate;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class StargateServerData extends PersistentState {

	private static final Deque<WeakReference<StargateServerData>> ALL = new ArrayDeque<>();

	static {
		ServerTickEvents.END_WORLD_TICK.register(world ->
				StargateServerData.get(world).tick());
	}

	private final Long2ObjectMap<Stargate> lookup = new Long2ObjectOpenHashMap<>();

	private StargateServerData() {
		ALL.add(new WeakReference<>(this));
	}

	private void tick() {
		for (Stargate stargate : this.lookup.values()) {
			stargate.tick();

			if (stargate.dirty()) {
				this.syncPartial(stargate);
				stargate.unmarkDirty();
			}
		}
	}

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

	public boolean contains(long address) {

	}

	public <R extends LongSupplier> R generateAddress(DirectedGlobalPos pos, Function<DirectedGlobalPos, R> func) {
		R address = null;

		while (address == null || contains(address.getAsLong())) {
			address = func.apply(pos);
		}

		return address;
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

	public static @NotNull StargateServerData getOrCreate(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(StargateServerData::loadNbt, StargateServerData::new, StargateMod.MOD_ID);
	}

	public static @Nullable StargateServerData get(ServerWorld world) {
        return world.getPersistentStateManager().get(StargateServerData::loadNbt, StargateMod.MOD_ID);
	}

	public static void tryGet(ServerWorld world, Consumer<StargateServerData> consumer) {
		StargateServerData data = get(world);

		if (data != null)
			consumer.accept(data);
	}
}
