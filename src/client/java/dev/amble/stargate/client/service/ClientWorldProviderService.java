package dev.amble.stargate.client.service;

import dev.amble.stargate.service.WorldProviderService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class ClientWorldProviderService extends WorldProviderService {

    @Override
    public World getWorld(RegistryKey<World> dimension, boolean isClient) {
        if (isClient) {
            ClientWorld world = MinecraftClient.getInstance().world;
            return world != null && dimension.equals(world.getRegistryKey()) ? world : null;
        }

        return super.getWorld(dimension, false);
    }
}
