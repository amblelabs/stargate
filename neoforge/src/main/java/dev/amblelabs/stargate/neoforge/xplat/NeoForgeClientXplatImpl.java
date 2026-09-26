package dev.amblelabs.stargate.neoforge.xplat;

import dev.amblelabs.stargate.xplat.ClientXplatAbstractions;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

public class NeoForgeClientXplatImpl implements ClientXplatAbstractions {

    @Override
    public void sendPacketToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }

    @Override
    public void setRenderLayer(Supplier<Block> supplier, RenderType renderType) {
        // Handled in block models.
    }

    @Override
    @SafeVarargs
    public final void setRenderLayer(RenderType renderType, Supplier<? extends Block>... suppliers) {
        // Handled in block models.
    }

    @Override
    public void initPlatformSpecific() {

    }

    @Override
    public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> type, EntityRendererProvider<T> provider) {
        EntityRenderers.register(type, provider);
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRenderers.register(type, provider);
    }

    @Override
    public void registerSkyRenderer(ResourceKey<Level> resourceKey, SkyRenderer skyRenderer) {
        // FIXME: no sky renderer
    }

    @Override
    public void registerItemProperty(Item item, ResourceLocation id, ItemPropertyFunction func) {
        ItemProperties.register(item, id, func);
    }
}
