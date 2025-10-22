package dev.amble.stargate.client;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.data.StargateClientData;
import dev.amble.stargate.client.command.ClientStargateDataCommand;
import dev.amble.stargate.client.command.ClientStargateDumpCommand;
import dev.amble.stargate.client.init.StargateClientYAARs;
import dev.amble.stargate.client.overlays.WormholeOverlay;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amble.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amble.stargate.client.renderers.RingBlockEntityRenderer;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.fluid.StargateFluids;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class StargateModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            ClientStargateDataCommand.register(dispatcher);
            ClientStargateDumpCommand.register(dispatcher);
        });

        registerBlockEntityRenderers();
        setupBlockRendering();
        registerEntityRenderers();

        WorldRenderEvents.AFTER_ENTITIES.register(PortalRendering::render);

        FluidRenderHandlerRegistry.INSTANCE.register(StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH,
                new SimpleFluidRenderHandler(
                        StargateMod.id("block/liquid_naquadah_still"),
                        StargateMod.id("block/liquid_naquadah_flow")
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH);

        HudRenderCallback.EVENT.register(new WormholeOverlay());

        StargateClientYAARs.init();
        StargateClientData.init();
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.GENERIC_STARGATE, StargateBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.RING, RingBlockEntityRenderer::new);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(StargateEntities.DHD_CONTROL_TYPE, DHDControlEntityRenderer::new);
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(StargateBlocks.GENERIC_GATE, RenderLayer.getCutout());
        map.putBlock(StargateBlocks.ORLIN_GATE, RenderLayer.getCutout());
    }
}

