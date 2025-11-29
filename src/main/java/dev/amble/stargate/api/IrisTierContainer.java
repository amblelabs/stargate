package dev.amble.stargate.api;

import dev.amble.lib.container.RegistryContainer;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.state.IrisTier;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public interface IrisTierContainer extends RegistryContainer<IrisTier> {

    RegistryKey<Registry<IrisTier>> REGISTRY_KEY = RegistryKey.ofRegistry(StargateMod.id("iris_tier"));
    SimpleDefaultedRegistry<IrisTier> REGISTRY = FabricRegistryBuilder.createDefaulted(REGISTRY_KEY, StargateMod.id("iron"))
            .buildAndRegister();

    @Override
    default void postProcessField(Identifier identifier, IrisTier value, Field field) {
        value.setRegistryKey(RegistryKey.of(REGISTRY_KEY, identifier));
    }

    @Override
    default Class<IrisTier> getTargetClass() {
        return IrisTier.class;
    }

    @Override
    default Registry<IrisTier> getRegistry() {
        return REGISTRY;
    }
}
