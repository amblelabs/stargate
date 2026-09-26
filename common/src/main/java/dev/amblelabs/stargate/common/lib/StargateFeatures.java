package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.StargateFeature;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public class StargateFeatures {

    private static final XplatRegistrar<Feature<?>> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.FEATURE);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Supplier<Feature<?>> STARGATE = feature("stargate", StargateFeature::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Feature<?>> Supplier<T> feature(String name, Supplier<T> feature) {
        return REGISTRAR.register(name, feature);
    }
}
