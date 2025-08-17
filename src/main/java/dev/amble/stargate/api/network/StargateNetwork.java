package dev.amble.stargate.api.network;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A list of all the known addresses and their corresponding Stargates.
 */
public abstract class StargateNetwork<T extends Stargate> {
	public static Identifier SYNC = StargateMod.id("sync");
	public static Identifier SYNC_ALL = StargateMod.id("sync_all");
	public static Identifier REMOVE = StargateMod.id("remove");

	protected final GateMap.Mutable<T> lookup = new GateMap.Mutable<>();

	protected StargateNetwork() {
	}

	/**
	 * Adds a new address to the phone book.
	 * @param stargate the Stargate to associate with the address
	 */
	protected boolean add(T stargate) {
		Address address = stargate.address();
		T prev = this.lookup.put(stargate);

		if (prev != null) {
			StargateMod.LOGGER.warn("Address {} already exists in the phone book!", address);
			return false;
		}

		return true;
	}

	public @Nullable T get(Address address) {
		return lookup.get(address);
	}

	public @Nullable T get(String address) {
		return lookup.get(address);
	}

	public @Nullable T get(UUID id) {
        return lookup.get(id);
	}

	/**
	 * Removes an address from the phone book.
	 * @param address the address to remove
	 * @return the Stargate associated with the address
	 */
	public Optional<Stargate> remove(Address address) {
		return Optional.ofNullable(this.lookup.remove(address));
	}

	/**
	 * Gets the nearest Stargate to the source within the given radius.
	 * @param source the source position
	 * @param radius the radius to search within
	 * @return the nearest Stargate, if one exists
	 */
	public Optional<Stargate> getNearTo(GlobalPos source, int radius) {
		for (Stargate gate : this.lookup.values()) {
			DirectedGlobalPos pos = gate.address().pos();

			if (pos.getDimension() != source.getDimension()) continue;

			// check if the blockpos is within radius
			if (pos.getPos().isWithinDistance(source.getPos(), radius)) {
				return Optional.of(gate);
			}
		}

		return Optional.empty();
	}

	public NbtCompound toNbt(boolean sync) {
		NbtCompound nbt = new NbtCompound();

		NbtList list = new NbtList();
		lookup.values().forEach(stargate -> list.add(stargate.toNbt(sync)));
		nbt.put("Stargates", list);

		return nbt;
	}

	public void fromNbt(NbtCompound nbt, boolean clear) {
		if (clear) this.lookup.clear();

		NbtList list = nbt.getList("Stargates", NbtElement.COMPOUND_TYPE);
		Stargate[] gates = this.fromNbtList(list);

		this.postProcessNbt(list, gates);
	}

    protected Stargate[] fromNbtList(NbtList list) {
		Stargate[] gates = new Stargate[list.size()];

		for (int i = 0; i < list.size(); i++) {
			try {
				NbtElement nbt = list.get(i);

				T stargate = this.fromNbt((NbtCompound) nbt);
				gates[i] = stargate;
			} catch (Exception e) {
				StargateMod.LOGGER.error("Failed to deserialize a stargate", e);
			}
		}

		return gates;
	}

	@SuppressWarnings("unchecked")
	protected void postProcessNbt(NbtList list, Stargate[] gates) {
		for (Stargate t : gates) {
			if (t == null)
				continue;

			this.lookup.put((T) t);
		}
	}

	protected abstract T fromNbt(NbtCompound nbt);

	public static <C, R> R with(BlockEntity entity, ContextManager<C, R> consumer) {
		return StargateNetwork.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(Entity entity, ContextManager<C, R> consumer) {
		return StargateNetwork.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(World world, ContextManager<C, R> consumer) {
		return StargateNetwork.with(world.isClient(), consumer, world::getServer);
	}

	@SuppressWarnings("unchecked")
	public static <C, R> R with(boolean isClient, ContextManager<C, R> consumer, Supplier<MinecraftServer> server) {
		StargateNetwork<?> manager = StargateNetwork.getInstance(!isClient);

		if (isClient) {
			return consumer.run((C) MinecraftClient.getInstance(), manager);
		} else {
			return consumer.run((C) server.get(), manager);
		}
	}

	public boolean isServer() {
		return this instanceof ServerStargateNetwork;
	}

	@Override
	public String toString() {
		return "StargateNetwork{" +
				"lookup=" + lookup +
				'}';
	}

	protected static StargateNetwork<?> getInstance(boolean isServer) {
		return isServer ? ServerStargateNetwork.get() : ClientStargateNetwork.get();
	}

	public static StargateNetwork<?> getInstance(World world) {
		return getInstance(!world.isClient());
	}

	@FunctionalInterface
	public interface ContextManager<C, R> {
		R run(C c, StargateNetwork<?> manager);
	}
}
