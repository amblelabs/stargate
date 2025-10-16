package dev.amble.stargate.api;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.address.GlobalAddressState;
import dev.amble.stargate.api.v3.state.address.LocalAddressState;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class StargateServerData extends PersistentState implements StargateData {

	// FIXME: might be not needed. Hopefully.
	private static final Deque<WeakReference<StargateServerData>> ALL = new ArrayDeque<>();

	public static void init() {
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			// FIXME: instead of ticking all gates, it might be better to tick only loaded gates...
			StargateServerData data = StargateServerData.get(world);

			if (data != null)
				data.tick();
		});
	}

	private final Long2ObjectMap<Stargate> lookup = new Long2ObjectOpenHashMap<>();
	private final Long2ObjectMap<ReferenceSet<Stargate>> chunk2Gates = new Long2ObjectOpenHashMap<>();
	private final WeakReference<ServerWorld> world;

	private StargateServerData(ServerWorld world) {
		ALL.add(new WeakReference<>(this));
		this.world = new WeakReference<>(world);
	}

	public void add(long address, Stargate stargate) {
		this.lookup.put(address, stargate);
	}

	public void mark(StargateLike gateLike) {
		Stargate stargate = gateLike.asGate();

		if (stargate != null)
			chunk2Gates.computeIfAbsent(ChunkPos.toLong(stargate.pos()),
					l -> new ReferenceOpenHashSet<>()).add(stargate);
	}

	public void unmark(StargateLike gateLike) {
		Stargate stargate = gateLike.asGate();

		if (stargate != null)
			chunk2Gates.get(ChunkPos.toLong(stargate.pos())).remove(stargate);
	}

	public @Nullable Collection<Stargate> findByChunk(int chunkX, int chunkZ) {
		return chunk2Gates.get(ChunkPos.toLong(chunkX, chunkZ));
	}

	private Collection<ServerPlayerEntity> tracking(Stargate stargate) {
		return PlayerLookup.tracking(world.get(), stargate.pos());
	}

	private void tick() {
		for (Stargate stargate : this.lookup.values()) {
			stargate.tick();

			if (stargate.dirty()) {
				this.syncPartial(stargate, tracking(stargate));
				stargate.unmarkDirty();
			}
		}
	}

	public static long getAnyId(Stargate stargate) {
		LongSupplier supplier = stargate.stateOrNull(GlobalAddressState.state);
		if (supplier != null) return supplier.getAsLong();

		supplier = stargate.stateOrNull(LocalAddressState.state);
		if (supplier != null) return supplier.getAsLong();

		throw new IllegalStateException("Gate " + stargate + " doesn't have an address state! What the fuck?");
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	public void syncPartial(Stargate gate, Collection<ServerPlayerEntity> targets) {
		NbtCompound nbt = new NbtCompound();
		gate.toNbt(nbt, true);

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVarLong(getAnyId(gate));
		buf.writeNbt(nbt);

		targets.forEach(player ->
				ServerPlayNetworking.send(player, SYNC, buf));
	}

	private void removePartial(Stargate gate, Collection<ServerPlayerEntity> targets) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVarLong(getAnyId(gate));

		targets.forEach(player ->
				ServerPlayNetworking.send(player, REMOVE, buf));
	}

	public <R extends LongSupplier> R generateAddress(RegistryKey<World> world, BlockPos pos, BiFunction<RegistryKey<World>, BlockPos, R> func) {
		R address = null;

		while (address == null || this.containsId(address.getAsLong())) {
			address = func.apply(world, pos);
		}

		return address;
	}

	@Override
	public void removeId(long id) {
		Stargate stargate = lookup.remove(id);
		Collection<Stargate> meta = this.chunk2Gates.get(ChunkPos.toLong(stargate.pos()));

		meta.remove(stargate);
	}

	@Override
	public boolean containsId(long id) {
		return lookup.containsKey(id);
	}

	@Override
	public @Nullable Stargate getById(long id) {
		return lookup.get(id);
	}

	private NbtCompound toNbt(boolean sync) {
		NbtCompound nbt = new NbtCompound();
		NbtList list = new NbtList();

		lookup.values().forEach(stargate -> {
			NbtCompound compound = new NbtCompound();
			stargate.toNbt(compound, sync);

			list.add(compound);
		});

		nbt.put("Network", list);
		return nbt;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		return this.toNbt(false);
	}

	public static StargateServerData loadNbt(ServerWorld world, NbtCompound nbt) {
		StargateServerData data = new StargateServerData(world);

		NbtList network = nbt.getList("Network", NbtElement.COMPOUND_TYPE);

		return data;
	}

	public static @NotNull StargateServerData getOrCreate(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(nbt -> loadNbt(world, nbt),
				() -> new StargateServerData(world), StargateMod.MOD_ID);
	}

	public static @Nullable StargateServerData get(ServerWorld world) {
		return world.getPersistentStateManager().get(nbt -> loadNbt(world, nbt), StargateMod.MOD_ID);
	}

	public static @Nullable StargateServerData getByGlobal(long globalAddress) {
		RegistryKey<World> key = AddressProvider.Global.getTarget(globalAddress);
		ServerWorld world = ServerLifecycleHooks.get().getWorld(key);

		return world != null ? StargateServerData.get(world) : null;
	}

	public static void accept(ServerWorld world, Consumer<StargateServerData> consumer) {
		StargateServerData data = get(world);
		if (data != null) consumer.accept(data);
	}

	public static <R> @Nullable R apply(ServerWorld world, Function<StargateServerData, R> func) {
		StargateServerData data = get(world);
		return data != null ? func.apply(data) : null;
	}
}
