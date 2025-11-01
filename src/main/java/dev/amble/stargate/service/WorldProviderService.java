package dev.amble.stargate.service;

import dev.amble.lib.util.ServerLifecycleHooks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class WorldProviderService {

    public static WorldProviderService INSTANCE = new WorldProviderService();

    public World getWorld(RegistryKey<World> world, boolean isClient) {
        if (isClient) throw new IllegalStateException("Attempted to get client world on server!");
        return ServerLifecycleHooks.get().getWorld(world);
    }
}
