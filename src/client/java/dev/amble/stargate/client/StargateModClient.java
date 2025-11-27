package dev.amble.stargate.client;

import dev.amble.stargate.block.DialingComputerBlock;
import dev.amble.stargate.client.api.data.StargateClientData;
import dev.amble.stargate.client.command.ClientStargateDataCommand;
import dev.amble.stargate.client.command.ClientStargateDumpCommand;
import dev.amble.stargate.client.init.StargateClientYAARs;
import dev.amble.stargate.client.renderers.*;
import dev.amble.stargate.client.renderers.overlays.WormholeOverlay;
import dev.amble.stargate.client.renderers.portal.PortalRendering;
import dev.amble.stargate.client.screen.DialingComputerScreen;
import dev.amble.stargate.client.service.ClientStargateDataProviderService;
import dev.amble.stargate.client.service.ClientWorldProviderService;
import dev.amble.stargate.client.service.TooltipServiceImpl;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateEntities;
import dev.amble.stargate.service.StargateDataProviderService;
import dev.amble.stargate.service.TooltipService;
import dev.amble.stargate.service.WorldProviderService;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

        ClientPlayNetworking.registerGlobalReceiver(DialingComputerBlock.OPEN, (client, handler, buf, sender) -> {
            client.setScreen(new DialingComputerScreen());
        });

        registerBlockEntityRenderers();
        setupBlockRendering();
        registerEntityRenderers();

        WorldRenderEvents.AFTER_ENTITIES.register(PortalRendering::render);
        HudRenderCallback.EVENT.register(new WormholeOverlay());

        StargateClientYAARs.init();
        StargateClientData.init();

        TooltipService.INSTANCE = new TooltipServiceImpl();
        StargateDataProviderService.INSTANCE = new ClientStargateDataProviderService();
        WorldProviderService.INSTANCE = new ClientWorldProviderService();
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.GENERIC_STARGATE, StargateBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.RING, RingBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(StargateBlockEntities.COMPUTER, DialingComputerRenderer::new);
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

