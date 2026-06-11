package dev.amblelabs.stargate.client.renderers.layers;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

/**
 * A custom implementation of {@link AutoGlowingGeoLayer} that uses {@link RenderType#beaconBeam(ResourceLocation, boolean)} instead of {@link RenderType#eyes(ResourceLocation)}.
 * @param <T>
 */
public class GlowRenderLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T> {

    public GlowRenderLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    protected @Nullable RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
        ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));
        return RenderType.beaconBeam(texture, true);
    }
}
