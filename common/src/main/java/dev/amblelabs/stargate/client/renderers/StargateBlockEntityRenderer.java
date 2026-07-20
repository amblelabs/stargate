package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.client.impl.ecs.state.GeckoState;
import dev.amblelabs.stargate.client.renderers.layers.GlowRenderLayer;
import dev.amblelabs.stargate.client.renderers.layers.GlyphRenderLayer;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StargateBlockEntityRenderer extends GeoBlockRenderer<StargateBlockEntity> {

    private static final ResourceLocation TEXTURE = StargateAPI.modLoc("textures/block/puddle.png");

    private final BlockRenderDispatcher blockRenderer;

    @SuppressWarnings("NotNullFieldNotInitialized")
    private GeoModel<StargateBlockEntity> model;

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super((GeoModel<StargateBlockEntity>) null);
        this.blockRenderer = context.getBlockRenderDispatcher();

        this.addRenderLayer(new GlyphRenderLayer<>(this));
        this.addRenderLayer(new GlowRenderLayer<>(this));
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
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getBlockSet() != null) {
            poseStack.pushPose();
            blockRenderer.renderSingleBlock(blockEntity.getBlockSet(), poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
        }

        Stargate stargate = blockEntity.stargate();
        if (stargate == null) return;

        GeckoState gecko = stargate.stateOrNull(GeckoState.state);
        if (gecko == null) return;

        this.model = gecko.geoModel;

        super.render(blockEntity, f, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, StargateBlockEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        // FIXME: should be rendered in an event, like the puddle particles
        if (isReRender || !StargateConfig.client().renderPuddleBackground())
            return;

        Stargate stargate = animatable.stargate();
        if (stargate == null || stargate.hasState(GateState.Closed.state)) return;

        float scale = 1f;

        GateState.Opening opening = stargate.stateOrNull(GateState.Opening.state);

        if (opening != null)
            scale = opening.timer / (float) GateState.Opening.TICKS_PER_KAWOOSH;

        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);

        StargateBlockEntityRenderer.renderQuad(poseStack, bufferSource.getBuffer(RenderType.breezeEyes(TEXTURE)),
                0xFFFFFF00 + (int) (255f / scale), scale,
                1, 6,
                -2.05f, 0.5f,
                3.05f, 0.5f,
                0, 1,
                0, 1
        );

        poseStack.popPose();
    }

    @SuppressWarnings("SameParameterValue")
    private static void renderQuad(PoseStack poseStack, VertexConsumer consumer, int color, float scale, float minY, float maxY, float minX, float minZ, float maxX, float maxZ, float minU, float maxU, float minV, float maxV) {
        float centerX = (minX + maxX) / 2;
        float centerY = (minY + maxY) / 2;
        float centerZ = (minZ + maxZ) / 2;

        poseStack.translate(centerX, centerY, centerZ);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-centerX, -centerY, -centerZ);

        PoseStack.Pose pose = poseStack.last();

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

    @Override
    public boolean shouldRenderOffScreen(StargateBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(StargateBlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply(1.0F, 0.0F, 1.0F).closerThan(cameraPos.multiply(1.0F, 0.0F, 1.0F), this.getViewDistance());
    }
}
