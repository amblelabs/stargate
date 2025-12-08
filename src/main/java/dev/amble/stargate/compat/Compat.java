package dev.amble.stargate.compat;

import dev.amble.lib.container.RegistryContainer;
import dev.amble.lib.container.impl.BlockContainer;
import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.compat.energy.EnergyHandler;
import dev.amble.stargate.compat.modonomicon.ModonomiconCompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

public class Compat implements ModInitializer {

	public static final List<Class<? extends ItemContainer>> items = new ArrayList<>();
	public static final List<Class<? extends BlockContainer>> blocks = new ArrayList<>();

	@Override
	public void onInitialize() {
		if (hasTechEnergy()) new EnergyHandler().onInitialize();
		if (hasModonomicon()) new ModonomiconCompat().onInitialize();

		for (Class<? extends ItemContainer> itemRegistry : items) {
			RegistryContainer.register(itemRegistry, StargateMod.MOD_ID);
		}

		for (Class<? extends BlockContainer> blockRegistry : blocks) {
			RegistryContainer.register(blockRegistry, StargateMod.MOD_ID);
		}
	}

	private static final boolean HAS_TECH_ENERGY = doesModExist("team_reborn_energy");
	private static final boolean HAS_MODONOMICON = doesModExist("modonomicon");
	private static final boolean HAS_IRIS = doesModExist("iris");

	public static boolean doesModExist(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	public static boolean hasTechEnergy() {
		return HAS_TECH_ENERGY;
	}

	public static boolean hasIris() {
		return HAS_IRIS;
	}

	public static boolean hasModonomicon() {
		return HAS_MODONOMICON;
	}
}