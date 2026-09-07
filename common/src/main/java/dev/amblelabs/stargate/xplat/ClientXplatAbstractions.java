package dev.amblelabs.stargate.xplat;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.joml.Matrix4f;

import java.util.ServiceLoader;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public interface ClientXplatAbstractions {
    void sendPacketToServer(CustomPacketPayload packet);

    void setRenderLayer(Block block, RenderType type);

    void initPlatformSpecific();

    <T extends Entity> void registerEntityRenderer(EntityType<? extends T> type, EntityRendererProvider<T> renderer);

    void registerSkyRenderer(ResourceKey<Level> resourceKey, SkyRenderer skyRenderer);

    @SuppressWarnings("deprecation")
    void registerItemProperty(Item item, ResourceLocation id, ItemPropertyFunction func);

    ClientXplatAbstractions INSTANCE = find();

    private static ClientXplatAbstractions find() {
        var providers = ServiceLoader.load(ClientXplatAbstractions.class).stream().toList();

        if (providers.size() != 1) {
            var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException(
                "There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
        } else {
            var provider = providers.getFirst();
            StargateAPI.LOGGER.debug("Instantiating client xplat impl: {}", provider.type().getName());
            return provider.get();
        }
    }

    @FunctionalInterface
    interface SkyRenderer {
        void renderSky(ClientLevel level, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera);
    }
}