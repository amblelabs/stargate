package dev.amblelabs.stargate.client.renderers;

import dev.amblelabs.stargate.common.entities.DHDControlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;

public class DHDControlEntityRenderer extends NoopRenderer<DHDControlEntity> {

    public DHDControlEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
