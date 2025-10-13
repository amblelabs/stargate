package dev.amble.stargate.api.network;

import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.v3.Stargate;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

public class StargateRef {
	private final long id;
	private final BooleanSupplier isClient;

	private Stargate cached;

	public StargateRef(long id, BooleanSupplier isClient) {
		this.id = id;
		this.isClient = isClient;
	}

	public StargateRef(long id, boolean isClient) {
		this(id, () -> isClient);
	}

	public StargateRef(Stargate stargate) {
		this(StargateServerData.getAnyId(stargate), stargate.isClient());
		this.cached = stargate;
	}

	public UUID id() {
		return id;
	}

	public Stargate get() {
		return cached != null ? cached : StargateNetwork.getInstance(!isClient.getAsBoolean()).get(id);
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
		T result = this.isPresent() ? consumer.apply(this.get()) : null;
		return Optional.ofNullable(result);
	}

	public void ifPresent(Consumer<Stargate> consumer) {
		if (this.isPresent())
			consumer.accept(this.get());
	}
}
