package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.StargateFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateFeatures {

    public static void registerFeatures(BiConsumer<Feature<?>, ResourceLocation> r) {
        for (var e : FEATURES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, Feature<?>> FEATURES = new LinkedHashMap<>();

    public static final Feature<?> STARGATE_DESTINY = feature("stargate_destiny", new StargateFeature(NoneFeatureConfiguration.CODEC));

    private static <T extends FeatureConfiguration> Feature<T> feature(String name, Feature<T> feature) {
        var id = modLoc(name);

        var old = FEATURES.put(id, feature);
        if (old != null) throw new IllegalArgumentException("Typo? Duplicate id " + name);

        return feature;
    }
}
