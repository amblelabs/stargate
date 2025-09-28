package dev.amble.stargate.compat.energy;

import dev.amble.stargate.init.StargateBlockEntities;
import team.reborn.energy.api.EnergyStorage;

public class EnergyHandler {
	public static void init() {
		EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> (be.hasStargate() && (be.gate().get() instanceof StargateRebornEnergy energy) ? energy.getStorage() : null), StargateBlockEntities.GENERIC_STARGATE);
		EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> (be.hasStargate() && (be.gate().get() instanceof StargateRebornEnergy energy) ? energy.getStorage() : null), StargateBlockEntities.ORLIN_GATE);
	}
}
