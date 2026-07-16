package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.renderers.layers.GlowRenderLayer;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class StargateBlockEntityRenderer extends GeoBlockRenderer<StargateBlockEntity> {

    private static final ResourceLocation TEXTURE = StargateAPI.modLoc("textures/block/puddle.png");

    @SuppressWarnings("NotNullFieldNotInitialized")
    private GeoModel<StargateBlockEntity> model;

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super((GeoModel<StargateBlockEntity>) null);

        this.addRenderLayer(new GlowRenderLayer<>(this));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
            @Override
            protected RenderType getRenderType(StargateBlockEntity animatable, @Nullable MultiBufferSource bufferSource) {
                ResourceLocation texture = AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable));
                return RenderType.beaconBeam(texture, true);
            }
        });
    }

    @Override
    public GeoModel<StargateBlockEntity> getGeoModel() {
        return model;
    }

    @Override
    protected Direction getFacing(StargateBlockEntity block) {
        return block.getBlockState().getValue(StargateBlock.FACING);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int i, int j) {
        Stargate stargate = blockEntity.stargate();
        if (stargate == null) return;

        GeckoState gecko = stargate.stateOrNull(GeckoState.state);
        if (gecko == null) return;

        this.model = gecko.geoModel;

        super.render(blockEntity, f, poseStack, bufferSource, i, j);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, StargateBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        if (isReRender)
            return;

        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);
        StargateBlockEntityRenderer.renderQuad(poseStack.last(), bufferSource.getBuffer(RenderType.breezeEyes(TEXTURE)), 0xFFFFFFFF, 1, 6, -2.05f, 0.5f, 3.05f, 0.5f, 0, 1, 0, 1);
        poseStack.popPose();
    }

    @SuppressWarnings("SameParameterValue")
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
