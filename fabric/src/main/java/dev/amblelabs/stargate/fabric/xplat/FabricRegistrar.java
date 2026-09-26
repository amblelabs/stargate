package dev.amblelabs.stargate.fabric.xplat;

import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar<B> implements XplatRegistrar<B> {

    private final Registry<B> registry;

    public FabricRegistrar(Registry<B> registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public FabricRegistrar(ResourceKey<Registry<B>> registryKey) {
        this((Registry<B>) BuiltInRegistries.REGISTRY.get(registryKey.location()));
    }

    @Override
    public <T extends B> Supplier<T> register(ResourceLocation id, Supplier<T> provider) {
        T value = provider.get();
        Registry.register(registry, id, value);
        return () -> value;
    }

    @Override
    public <T extends B> Holder<B> registerHolder(ResourceLocation id, Supplier<T> provider) {
        T value = provider.get();
        Registry.register(registry, id, value);
        return registry.wrapAsHolder(value);
    }

    @Override
    public void registerAll() { }
}
