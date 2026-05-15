package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.renderers.layers.GlyphRenderLayer;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class StargateBlockEntityRenderer extends GeoBlockRenderer<StargateBlockEntity> {

    private GeoModel<StargateBlockEntity> model;

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super((GeoModel<StargateBlockEntity>) null);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.addRenderLayer(new GlyphRenderLayer<>(this));
    }

    @Override
    public GeoModel<StargateBlockEntity> getGeoModel() {
        return model;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage") // im a magic man
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        GeckoState gecko = blockEntity.stargate.stateOrNull(GeckoState.state);
        if (gecko == null) return;

        this.model = gecko.geoModel;
        super.render(blockEntity, f, poseStack, multiBufferSource, i, j);
    }
}
