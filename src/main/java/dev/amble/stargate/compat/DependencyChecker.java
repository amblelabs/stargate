package dev.amble.stargate.compat;

import net.fabricmc.loader.api.FabricLoader;

public class DependencyChecker {
    private static final boolean HAS_TECH_ENERGY = doesModExist("team_reborn_energy");
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
}