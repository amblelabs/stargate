package dev.amble.stargate.service;

import dev.amble.lib.util.ServerLifecycleHooks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface WorldProviderService {

    default World getWorld(RegistryKey<World> world, boolean isClient) {
        if (isClient) throw new IllegalStateException("Attempted to get client world on server!");
        return ServerLifecycleHooks.get().getWorld(world);
    }
}
