package dev.amble.stargate.api.network;

import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Disposable;
import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class StargateRef implements Disposable {
	private final LoadFunc load;
	private final UUID id;

	private Stargate cached;

	public StargateRef(UUID id, LoadFunc load) {
		this.id = id;
		this.load = load;
	}

	public StargateRef(Stargate stargate) {
		this(stargate.address().id(), uuid -> StargateNetwork.getInstance(!stargate.isClient()).get(uuid));
		this.cached = stargate;
	}

	public StargateRef(Address address, LoadFunc load) {
		this(address.id(), load);
	}

	public UUID id() {
		return id;
	}

	public Stargate get() {
		if (this.cached != null && !this.shouldInvalidate())
			return this.cached;

		this.cached = this.load.apply(this.id);
		return this.cached;
	}

	private boolean shouldInvalidate() {
		return this.cached.isAged();
	}

	public boolean isPresent() {
		return this.get() != null;
	}

	public boolean isEmpty() {
		return this.get() == null;
	}

	/**
	 * @return the result of the function, {@literal null} otherwise.
	 */
	public <T> Optional<T> apply(Function<Stargate, T> consumer) {
		if (this.isPresent())
			return Optional.of(consumer.apply(this.cached));

		return Optional.empty();
	}

	public void ifPresent(Consumer<Stargate> consumer) {
		if (this.isPresent())
			consumer.accept(this.get());
	}

	public static StargateRef createAs(Entity entity, UUID uuid) {
		return new StargateRef(uuid,
				real -> StargateNetwork.with(entity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(BlockEntity blockEntity, UUID uuid) {
		return new StargateRef(uuid,
				real -> StargateNetwork.with(blockEntity, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(World world, UUID uuid) {
		return new StargateRef(uuid, real -> StargateNetwork.with(world, (o, manager) -> manager.get(real)));
	}

	public static StargateRef createAs(boolean isClient, UUID uuid) {
		return new StargateRef(uuid, real -> StargateNetwork.getInstance(!isClient).get(real));
	}

	@Override
	public void dispose() {
		this.cached = null;
	}

	public interface LoadFunc extends Function<UUID, Stargate> {
	}
}
