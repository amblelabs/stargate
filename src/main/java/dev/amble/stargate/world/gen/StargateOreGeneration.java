package dev.amble.stargate.world.gen;

import dev.amble.stargate.world.StargatePlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class StargateOreGeneration {

    public static void generateOres(){
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.DESERT),
                GenerationStep.Feature.UNDERGROUND_ORES, StargatePlacedFeatures.NAQUADAH_ORE_PLACED_KEY);
    }
}
