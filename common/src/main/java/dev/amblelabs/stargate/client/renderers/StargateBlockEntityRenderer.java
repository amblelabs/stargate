package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.renderers.layers.GlyphRenderLayer;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.util.ClientUtil;

public class StargateBlockEntityRenderer extends GeoBlockRenderer<StargateBlockEntity> {

    private GeoModel<StargateBlockEntity> model;
    private static final ResourceLocation TEXTURE = StargateAPI.modLoc("textures/block/puddle.png");

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super((GeoModel<StargateBlockEntity>) null);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(StargateBlockEntity animatable, @Nullable MultiBufferSource bufferSource) {
                ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));
                return RenderType.beaconBeam(texture, true);
            }
        });

        this.addRenderLayer(new GlyphRenderLayer<>(this));
    }

    @Override
    public GeoModel<StargateBlockEntity> getGeoModel() {
        return model;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        GeckoState gecko = blockEntity.stargate.stateOrNull(GeckoState.state);
        if (gecko == null) return;

        this.model = gecko.geoModel;

        VertexConsumer vc = multiBufferSource.getBuffer(RenderType.breezeEyes(TEXTURE));

        poseStack.pushPose();
        StargateBlockEntityRenderer.renderQuad(poseStack.last(), vc, 0xFFFFFFFF, 1, 6, -2.05f, 0.5f, 3.05f, 0.5f, 0, 1, 0, 1);
        poseStack.popPose();

        super.render(blockEntity, f, poseStack, multiBufferSource, i, j);
    }

    private static void renderQuad(PoseStack.Pose pose, VertexConsumer consumer, int color, float minY, float maxY, float minX, float minZ, float maxX, float maxZ, float minU, float maxU, float minV, float maxV) {
        addVertex(pose, consumer, color, maxY, minX, minZ, maxU, minV);
        addVertex(pose, consumer, color, minY, minX, minZ, maxU, maxV);
        addVertex(pose, consumer, color, minY, maxX, maxZ, minU, maxV);
        addVertex(pose, consumer, color, maxY, maxX, maxZ, minU, minV);
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer consumer, int color, float y, float x, float z, float u, float v) {
        consumer.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0xF000F0)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
