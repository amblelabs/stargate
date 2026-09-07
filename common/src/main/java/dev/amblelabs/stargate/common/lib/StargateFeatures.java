package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.StargateFeature;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public class StargateFeatures {

    private static final XplatRegister<Feature<?>> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.FEATURE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<Feature<?>> STARGATE = feature("stargate", StargateFeature::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Feature<?>> Supplier<T> feature(String name, Supplier<T> feature) {
        return REGISTER.register(name, feature);
    }
}
