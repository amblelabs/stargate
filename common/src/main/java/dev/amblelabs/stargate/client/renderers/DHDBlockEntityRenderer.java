package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.renderers.layers.GlyphRenderLayer;
import dev.amblelabs.stargate.common.blocks.DHDBlockEntity;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DHDBlockEntityRenderer extends GeoBlockRenderer<DHDBlockEntity> {

    private GeoModel<DHDBlockEntity> model;

    public DHDBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super((GeoModel<DHDBlockEntity>) null);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(DHDBlockEntity animatable, @Nullable MultiBufferSource bufferSource) {
                ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));
                return RenderType.eyes(texture);
            }
        });

        // this.addRenderLayer(new GlyphRenderLayer<>(this));
    }

    @Override
    public GeoModel<DHDBlockEntity> getGeoModel() {
        return model;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(DHDBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        GeckoState gecko = blockEntity.container.stateOrNull(GeckoState.state);
        if (gecko == null) return;

        this.model = gecko.geoModel;

        super.render(blockEntity, f, poseStack, multiBufferSource, i, j);
    }
}
