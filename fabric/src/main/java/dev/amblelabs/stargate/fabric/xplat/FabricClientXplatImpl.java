package dev.amblelabs.stargate.fabric.xplat;

import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class FabricClientXplatImpl implements IClientXplatAbstractions {

    @Override
    public void sendPacketToServer(CustomPacketPayload packet) {
        ClientPlayNetworking.send(packet);
    }

    @Override
    public void setRenderLayer(Block block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }

    @Override
    public void initPlatformSpecific() {

    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> type,
                                                          EntityRendererProvider<T> renderer) {
        EntityRendererRegistry.register(type, renderer);
    }

    @Override
    public void registerSkyRenderer(ResourceKey<Level> resourceKey, SkyRenderer skyRenderer) {
        DimensionRenderingRegistry.registerSkyRenderer(resourceKey, context -> {
            skyRenderer.renderSky(context.world(), context.positionMatrix(), context.projectionMatrix(), context.camera().getPartialTickTime(), context.camera());
        });
    }

    @SuppressWarnings("deprecation")
    private record UnclampedClampedItemPropFunc(ItemPropertyFunction inner) implements ClampedItemPropertyFunction {
        @Override
        public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity,
                                   int seed) {
            return inner.call(stack, level, entity, seed);
        }

        @Override
        public float call(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            return this.unclampedCall(stack, level, entity, seed);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void registerItemProperty(Item item, ResourceLocation id, ItemPropertyFunction func) {
        ItemProperties.register(item, id, new UnclampedClampedItemPropFunc(func));
    }
}
