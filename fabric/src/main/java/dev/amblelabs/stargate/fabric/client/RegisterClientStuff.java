package dev.amblelabs.stargate.fabric.client;

import dev.amblelabs.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.skybox.AbydosSkyRenderer;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
        registerer.registerBlockEntityRenderer(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);

//        registerer.registerBlockEntityRenderer(AitBlockEntities.DOOR_BLOCK_ENTITY, DoorBlockEntityRenderer::new);
//        registerer.registerBlockEntityRenderer(AitBlockEntities.CONSOLE_BLOCK_ENTITY, ConsoleBlockEntityRenderer::new);
    }

    @FunctionalInterface
    public interface BlockEntityRendererRegisterer {
        <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type,
            BlockEntityRendererProvider<? super T> provider);
    }
}