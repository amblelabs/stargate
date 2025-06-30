package dev.amble.stargate.mixins.compat.energy;

import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.v2.StargateKernel;
import dev.amble.stargate.compat.energy.StargateRebornEnergy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@Mixin(value = StargateKernel.Basic.class, remap = false)
public abstract class EnergyStargateMixin implements StargateRebornEnergy, StargateKernel {
	@Unique
	public final SimpleEnergyStorage r$energy = new SimpleEnergyStorage(100000, 32, 0) {
		@Override
		protected void onFinalCommit() {
			EnergyStargateMixin.this.energy = (int) this.amount;
			EnergyStargateMixin.this.parent.markDirty();
		}
	};

	@Shadow
	protected int energy;

	@Shadow
	protected Stargate parent;

	@Override
	public EnergyStorage getStorage() {
		return r$energy;
	}
}
