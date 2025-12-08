package dev.amble.stargate.compat.energy;

import dev.amble.stargate.init.StargateBlockEntities;
import net.fabricmc.api.ModInitializer;
import team.reborn.energy.api.EnergyStorage;

public class EnergyHandler implements ModInitializer {

	@Override
	public void onInitialize() {
		EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> (be.isLinked() && (be.asGate() instanceof StargateRebornEnergy energy) ? energy.getStorage() : null), StargateBlockEntities.GENERIC_STARGATE);
	}


}
