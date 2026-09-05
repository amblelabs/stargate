package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.amblelabs.stargate.common.blocks.StargateRingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;

public class StargateRingBlockEntityRenderer implements BlockEntityRenderer<StargateRingBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public StargateRingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(StargateRingBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() == null) return;
        ProfilerFiller profiler = blockEntity.getLevel().getProfiler();
        profiler.push("stargate:blockset");

        this.render0(profiler, blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        profiler.pop();
    }

    private void render0(ProfilerFiller profiler, StargateRingBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getBlockSet() == null) return;
        profiler.push(() -> blockEntity.getBlockSet().getBlockHolder().getRegisteredName());

        poseStack.pushPose();
        blockRenderer.renderSingleBlock(blockEntity.getBlockSet(), poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        profiler.pop();
    }
}