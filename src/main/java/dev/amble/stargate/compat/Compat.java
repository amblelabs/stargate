package dev.amble.stargate.compat;

import dev.amble.stargate.compat.energy.EnergyHandler;
import net.fabricmc.api.ModInitializer;

public class Compat implements ModInitializer {
	@Override
	public void onInitialize() {
		if (DependencyChecker.hasTechEnergy()) {
			EnergyHandler.init();
		}
	}
}