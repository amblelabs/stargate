package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.ecs.Prototype;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public interface StargateRegistries {
    ResourceKey<Registry<Prototype>> PROTOTYPE = ResourceKey.createRegistryKey(modLoc("prototype"));
}
