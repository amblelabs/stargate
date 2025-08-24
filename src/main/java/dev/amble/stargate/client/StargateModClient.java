package dev.amble.stargate.client;

import dev.amble.lib.register.AmbleRegistries;
import dev.amble.stargate.api.network.ClientStargateNetwork;
import dev.amble.stargate.api.GlyphOriginRegistry;
import dev.amble.stargate.block.AbstractStargateBlock;
import dev.amble.stargate.client.command.ClientStargateDataCommand;
import dev.amble.stargate.client.command.ClientStargateDumpCommand;
import dev.amble.stargate.client.overlays.WormholeOverlay;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amble.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amble.stargate.client.renderers.RingBlockEntityRenderer;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateEntities;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.core.fluid.StargateFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class StargateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AmbleRegistries.getInstance().registerAll(
                GlyphOriginRegistry.getInstance()
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            ClientStargateDataCommand.register(dispatcher);
            ClientStargateDumpCommand.register(dispatcher);
        });

        ClientStargateNetwork.get();
        registerBlockEntityRenderers();
        setupBlockRendering();
        registerEntityRenderers();

        WorldRenderEvents.AFTER_ENTITIES.register(this::portalBOTI);

        FluidRenderHandlerRegistry.INSTANCE.register(StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH,
                new SimpleFluidRenderHandler(
                        new Identifier("stargate:block/liquid_naquadah_still"),
                        new Identifier("stargate:block/liquid_naquadah_flow")
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH);

        HudRenderCallback.EVENT.register(new WormholeOverlay());

    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.MILKY_WAY_STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.ORLIN_STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.PEGASUS_STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.DESTINY_STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.RING, RingBlockEntityRenderer::new);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(StargateEntities.DHD_CONTROL_TYPE, DHDControlEntityRenderer::new);
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(StargateBlocks.MILKY_WAY_STARGATE, RenderLayer.getCutout());
        map.putBlock(StargateBlocks.ORLIN_STARGATE, RenderLayer.getCutout());
        map.putBlock(StargateBlocks.PEGASUS_STARGATE, RenderLayer.getCutout());
        map.putBlock(StargateBlocks.DESTINY_STARGATE, RenderLayer.getCutout());
    }

    public void portalBOTI(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        MatrixStack stack = context.matrixStack();
        for (StargateBlockEntity gate : PortalRendering.PORTAL_RENDER_QUEUE) {
            if (gate == null || !gate.hasStargate()) continue;
            Vec3d pos = gate.getPos().toCenterPos();
            stack.push();
            stack.translate(pos.getX() - context.camera().getPos().getX(),
                    pos.getY() - context.camera().getPos().getY(), pos.getZ() - context.camera().getPos().getZ());
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(gate.getCachedState().get(AbstractStargateBlock.FACING).asRotation()));
            stack.translate(0, -2f, 0);
            PortalRendering.renderPortal(gate, gate.gate().get().state(), stack);
            stack.pop();
        }
        PortalRendering.PORTAL_RENDER_QUEUE.clear();
    }
}

