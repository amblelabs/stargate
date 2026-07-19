package dev.amblelabs.stargate.fabric.client;

import dev.amblelabs.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amblelabs.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.StargateRingBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.skybox.AbydosSkyRenderer;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateEntities;
import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RegisterClientStuff {

    @SuppressWarnings("EmptyMethod")
    public static void init() {
        var x = IClientXplatAbstractions.INSTANCE;
        x.registerSkyRenderer(AbydosSkyRenderer.DIMENSION_KEY, new AbydosSkyRenderer());
    }

    public static void registerBlockEntityRenderers(BlockEntityRendererRegisterer registerer) {
        registerer.registerBlockEntityRenderer(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
        registerer.registerBlockEntityRenderer(StargateBlockEntities.RING, StargateRingBlockEntityRenderer::new);
        registerer.registerBlockEntityRenderer(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);
    }

    public static void registerEntityRenderers(EntityRendererRegisterer registerer) {
        registerer.registerEntityRenderer(StargateEntities.DHD_CONTROL, DHDControlEntityRenderer::new);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegisterer {
        <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type,
            BlockEntityRendererProvider<? super T> provider);
    }

    @FunctionalInterface
    public interface EntityRendererRegisterer {
        <T extends Entity> void registerEntityRenderer(EntityType<T> type,
                                                            EntityRendererProvider<? super T> provider);
    }
}