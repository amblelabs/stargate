package dev.amble.stargate.client;

import dev.amble.lib.register.AmbleRegistries;
import dev.amble.stargate.api.ClientStargateNetwork;
import dev.amble.stargate.api.PointOfOriginRegistry;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amble.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.ClientStargateUtil;
import dev.amble.stargate.core.StargateBlockEntities;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateEntities;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import dev.amble.stargate.core.fluid.StargateFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;

public class StargateModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AmbleRegistries.getInstance().registerAll(
                PointOfOriginRegistry.getInstance()
        );

        ClientStargateNetwork.getInstance();
        registerBlockEntityRenderers();
        setupBlockRendering();
        registerEntityRenderers();

        ClientStargateUtil.init();

        WorldRenderEvents.AFTER_ENTITIES.register(this::portalBOTI);

        FluidRenderHandlerRegistry.INSTANCE.register(StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/lava_still"),
                        new Identifier("minecraft:block/lava_flow"),
                        0xA1E038D0
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
                StargateFluids.STILL_LIQUID_NAQUADAH, StargateFluids.FLOWING_LIQUID_NAQUADAH);

    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(StargateEntities.DHD_CONTROL_TYPE, DHDControlEntityRenderer::new);
    }

    public static void setupBlockRendering() {
        BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
        map.putBlock(StargateBlocks.STARGATE, RenderLayer.getCutout());
    }

    public void portalBOTI(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        SinglePartEntityModel contents = new StargateModel(StargateModel.getTexturedModelData().createModel());
        if (client.player == null || client.world == null) return;
        MatrixStack stack = context.matrixStack();
        for (StargateBlockEntity painting : PortalRendering.PORTAL_RENDER_QUEUE) {
            if (painting == null) continue;
            Vec3d pos = painting.getPos().toCenterPos();
            stack.push();
            stack.translate(pos.getX() - context.camera().getPos().getX(),
                    pos.getY() - context.camera().getPos().getY(), pos.getZ() - context.camera().getPos().getZ());
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(painting.getCachedState().get(StargateBlock.FACING).asRotation()));
            stack.translate(0, -2f, 0);
            PortalRendering.renderPortal(painting, painting.getGateState(), stack);
            stack.pop();
        }
        PortalRendering.PORTAL_RENDER_QUEUE.clear();
    }
}

