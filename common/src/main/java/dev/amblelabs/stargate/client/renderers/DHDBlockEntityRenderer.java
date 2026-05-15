package dev.amblelabs.stargate.client.renderers;

import dev.amblelabs.stargate.client.models.DHDGeoModel;
import dev.amblelabs.stargate.common.blocks.DHDBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DHDBlockEntityRenderer extends GeoBlockRenderer<DHDBlockEntity> {

    public DHDBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new DHDGeoModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(DHDBlockEntity animatable, @Nullable MultiBufferSource bufferSource) {
                ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));
                return RenderType.beaconBeam(texture, true);
            }
        });
    }
}
