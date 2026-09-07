package dev.amblelabs.stargate.client;

import dev.amblelabs.stargate.client.renderers.DHDBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.DHDControlEntityRenderer;
import dev.amblelabs.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.StargateRingBlockEntityRenderer;
import dev.amblelabs.stargate.client.renderers.skybox.AbydosSkyRenderer;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateEntities;
import dev.amblelabs.stargate.xplat.ClientXplatAbstractions;
import net.minecraft.client.renderer.RenderType;

@SuppressWarnings("unchecked")
public class RegisterClientStuff {

    public static void init() {
        ClientXplatAbstractions.INSTANCE.registerSkyRenderer(AbydosSkyRenderer.DIMENSION_KEY, new AbydosSkyRenderer());

        var x = ClientXplatAbstractions.INSTANCE;

        x.setRenderLayer(RenderType.cutout(), StargateBlocks.DRY_BUSH, StargateBlocks.DRY_GRASS);

        x.registerBlockEntityRenderer(StargateBlockEntities.STARGATE, StargateBlockEntityRenderer::new);
        x.registerBlockEntityRenderer(StargateBlockEntities.RING, StargateRingBlockEntityRenderer::new);
        x.registerBlockEntityRenderer(StargateBlockEntities.DHD, DHDBlockEntityRenderer::new);

        x.registerEntityRenderer(StargateEntities.DHD_CONTROL, DHDControlEntityRenderer::new);
    }
}
