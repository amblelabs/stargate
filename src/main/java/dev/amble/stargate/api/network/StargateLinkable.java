package dev.amble.stargate.api.network;

import dev.amble.stargate.api.StargateLike;
import dev.amble.stargate.api.v3.Stargate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Indicates that this class holds a stargate
 */
public interface StargateLinkable extends StargateLike {

	@Override
	@Nullable Stargate asGate();

	default void acceptGate(Consumer<Stargate> consumer) {
		Stargate gate = this.asGate();
		if (gate != null) consumer.accept(gate);
	}

	default <R> @Nullable R applyGate(Function<Stargate, R> func) {
		Stargate gate = this.asGate();
		return gate != null ? func.apply(gate) : null;
	}

	default boolean isLinked() {
		return asGate() != null;
	}

	void link(@NotNull Stargate gate);
	void link(long address);
}
