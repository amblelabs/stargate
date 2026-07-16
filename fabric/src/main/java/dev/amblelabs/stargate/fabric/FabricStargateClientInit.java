package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.lib.StargateClientEcs;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.fabric.client.RegisterClientStuff;
import dev.amblelabs.stargate.fabric.network.FabricPacketHandler;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class FabricStargateClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricPacketHandler.initClient();

        FabricLoader.getInstance().getModContainer(StargateAPI.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(StargateAPI.modLoc("menu"), container, ResourcePackActivationType.DEFAULT_ENABLED);
        });

//        HudRenderCallback.EVENT.register(AitAdditionalRenderers::overlayGui);

        // TODO: move stuff from RegisterClientStuff to here
        RegisterClientStuff.init();
        BlockRenderLayerMap.INSTANCE.putBlock(StargateBlocks.DRY_BUSH,
                RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(StargateBlocks.DRY_GRASS,
                RenderType.cutout());

//        YoureFiredModelLayers.init((loc, def) -> EntityModelLayerRegistry.registerModelLayer(loc, def::get));

        StargateParticles.FactoryHandler.registerFactories(new StargateParticles.FactoryHandler.Consumer() {

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                ParticleFactoryRegistry.getInstance().register(type, constructor::apply);
            }
        });

        RegisterClientStuff.registerBlockEntityRenderers(BlockEntityRenderers::register);

        StargateInterop.clientInit();
//        RegisterClientStuff.registerColorProviders(
//                ColorProviderRegistry.ITEM::register,
//                ColorProviderRegistry.BLOCK::register
//        );

//        YoureFiredBlocks.registerBlocks((block, resourceLocation) -> {
//            if (!(block instanceof GenericBurningBlock)) return;
//            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
//        });

        StargateClientEcs.registerAll();
    }
}
