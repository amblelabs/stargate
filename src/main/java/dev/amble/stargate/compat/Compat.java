package dev.amble.stargate.compat;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.compat.ait.AITHandler;
import dev.amble.stargate.compat.energy.EnergyHandler;
import net.fabricmc.api.ModInitializer;

public class Compat implements ModInitializer {
	@Override
	public void onInitialize() {
		if (DependencyChecker.hasTechEnergy()) {
			EnergyHandler.init();
			StargateMod.LOGGER.info("ENERGY COMPAT LOADED");
		}

		if (DependencyChecker.hasAIT()) {
			AITHandler.getInstance().init();
			StargateMod.LOGGER.info("AIT COMPAT LOADED");
		}
	}
}