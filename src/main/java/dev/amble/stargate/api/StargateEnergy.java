package dev.amble.stargate.api;

import dev.amble.lib.data.DistanceInformation;

public interface StargateEnergy {
	long getEnergy();
	long getMaxEnergy();

	/**
	 * @param address the address to dial
	 * @return the amount of energy required to dial the given address
	 */
	long getRequiredEnergy(Address address);

	/**
	 * @param address the address to dial
	 * @return whether this stargate has enough energy to dial the given address
	 */
	default boolean hasEnoughEnergy(Address address) {
		return this.getEnergy() >= this.getRequiredEnergy(address);
	}
	void setEnergy(long amount);

	default void addEnergy(long amount) {
		this.setEnergy(this.getEnergy() + amount);
	}
	default void removeEnergy(long amount) {
		this.addEnergy(-amount);
	}

	static long getRequiredEnergy(Address source, Address target, long limit) {
		DistanceInformation distance = source.distanceTo(target);
		long energy = (long) (limit * Math.exp(-distance.distance() * 0.025f) + 25000);
		if (distance.dimChange()) {
			energy += 1000; // Add a set amount if dimChanged
		}
		if (distance.rotChange()) {
			energy += 500; // Add a set amount if rotChanged
		}
		return energy;
	}
}
