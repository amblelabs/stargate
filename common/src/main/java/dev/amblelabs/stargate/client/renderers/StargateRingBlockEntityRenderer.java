package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.amblelabs.stargate.common.blocks.StargateRingBlockEntity;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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

        this.render0(profiler, blockEntity, poseStack, bufferSource);

        profiler.pop();
    }

    private void render0(ProfilerFiller profiler, StargateRingBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource) {
        BlockState state = blockEntity.getBlockSet();
        Level level = blockEntity.getLevel();

        if (state == null || level == null) return;

        profiler.push(() -> state.getBlockHolder().getRegisteredName());

        poseStack.pushPose();
        RenderType type = ItemBlockRenderTypes.getChunkRenderType(state);
        VertexConsumer consumer = bufferSource.getBuffer(type);

        // renders the fake block with AO and other fancy stuff
        blockRenderer.renderBatched(state, blockEntity.getBlockPos(), level, poseStack, consumer, true, level.getRandom());
        poseStack.popPose();

        profiler.pop();
    }
}