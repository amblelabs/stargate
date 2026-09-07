package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.RegisterClientStuff;
import dev.amblelabs.stargate.client.lib.StargateClientEcs;
import dev.amblelabs.stargate.client.renderers.*;
import dev.amblelabs.stargate.client.renderers.skybox.AbydosSkyRenderer;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateEntities;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.fabric.network.FabricPacketHandler;
import dev.amblelabs.stargate.interop.StargateInterop;
import dev.amblelabs.stargate.xplat.ClientXplatAbstractions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class FabricStargateClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricPacketHandler.initClient();

        FabricLoader.getInstance().getModContainer(StargateAPI.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(StargateAPI.modLoc("menu"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });

        WorldRenderEvents.START.register(context -> RenderDeduper.clear());
        HudRenderCallback.EVENT.register(StargateAdditionalRenderers::overlayGui);

        RegisterClientStuff.init();

        StargateParticles.FactoryHandler.registerFactories(new StargateParticles.FactoryHandler.Consumer() {

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                ParticleFactoryRegistry.getInstance().register(type, constructor::apply);
            }
        });

        StargateInterop.clientInit();

        StargateClientEcs.registerAll();
    }
}
