package dev.amblelabs.stargate.fabric.xplat;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class FabricRegister<B> implements XplatRegister<B> {

    private final Registry<B> registry;

    public FabricRegister(Registry<B> registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public FabricRegister(ResourceKey<Registry<B>> registryKey) {
        this((Registry<B>) BuiltInRegistries.REGISTRY.get(registryKey.location()));
    }

    @Override
    public <T extends B> Supplier<T> register(String id, Supplier<T> provider) {
        T value = provider.get();
        Registry.register(registry, StargateAPI.modLoc(id), value);
        return () -> value;
    }

    @Override
    public <T extends B> Holder<B> registerHolder(String id, Supplier<T> provider) {
        T value = provider.get();
        Registry.register(registry, StargateAPI.modLoc(id), value);
        return registry.wrapAsHolder(value);
    }

    @Override
    public void registerAll() { }
}
