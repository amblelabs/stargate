package dev.amble.stargate.api.v2;

import dev.amble.stargate.StargateMod;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;

public class GateKernelRegistry {

    private static final RegistryKey<Registry<KernelCreator>> KEY = RegistryKey.ofRegistry(StargateMod.id("kernel"));
    private static final SimpleRegistry<KernelCreator> REGISTRY = FabricRegistryBuilder.createSimple(KEY).buildAndRegister();

    public static SimpleRegistry<KernelCreator> get() {
        return REGISTRY;
    }

    public static void init() {
        Registry.register(REGISTRY, MilkyWayGateKernel.ID, MilkyWayGateKernel::new);
    }

    @FunctionalInterface
    public interface KernelCreator {
        StargateKernel.Impl create(Stargate stargate);
    }
}
