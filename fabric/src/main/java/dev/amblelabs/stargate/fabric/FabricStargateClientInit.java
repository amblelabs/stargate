package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.client.lib.StargateClientEcs;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.fabric.client.RegisterClientStuff;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.*;

import java.util.*;

public class FabricStargateClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        HudRenderCallback.EVENT.register(AitAdditionalRenderers::overlayGui);

        RegisterClientStuff.init();

//        YoureFiredModelLayers.init((loc, def) -> EntityModelLayerRegistry.registerModelLayer(loc, def::get));

        StargateParticles.FactoryHandler.registerFactories((type, constructor) ->
                ParticleFactoryRegistry.getInstance().register(type, constructor::apply));

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
