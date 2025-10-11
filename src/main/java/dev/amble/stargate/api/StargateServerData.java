package dev.amble.stargate.api;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.network.ServerStargateNetwork;
import dev.amble.stargate.api.v3.Stargate;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class StargateServerData extends PersistentState implements StargateData {

	private static final Deque<WeakReference<StargateServerData>> ALL = new ArrayDeque<>();

	public static void init() {
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			StargateServerData data = StargateServerData.get(world);

			if (data != null)
				data.tick();
		});
	}

	private final Long2ObjectMap<Stargate> lookup = new Long2ObjectOpenHashMap<>();

	private StargateServerData() {
		ALL.add(new WeakReference<>(this));
	}

	@Override
	public boolean isDirty() {
		return true;
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

	public <R extends LongSupplier> R generateAddress(DirectedGlobalPos pos, Function<DirectedGlobalPos, R> func) {
		R address = null;

		while (address == null || this.containsId(address.getAsLong())) {
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

	@Override
	public void removeId(long id) {
		lookup.remove(id);
	}

	@Override
	public boolean containsId(long id) {
		return lookup.containsKey(id);
	}

	@Override
	public @Nullable Stargate getById(long id) {
		return lookup.get(id);
	}
}
