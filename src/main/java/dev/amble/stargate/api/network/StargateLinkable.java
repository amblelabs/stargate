package dev.amble.stargate.api.network;

import net.minecraft.world.World;

/**
 * Indicates that this class holds a stargate
 */
public interface StargateLinkable {
	StargateRef gate();

	default boolean hasStargate() {
		return this.gate() != null && this.gate().isPresent();
	}

	default boolean isLinked() {
		return this.hasStargate();
	}

	void setStargate(StargateRef gate);

	default void link(StargateRef gate) {
		this.setStargate(gate);
		this.onLinked();
	}

	default void link(String id, World world) {
		this.link(StargateRef.createAs(world, id));
	}

	default void onLinked() {

	}
}
