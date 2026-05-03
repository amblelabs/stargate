package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        poseStack.pushPose();
        poseStack.scale(1, -1, -1);
        // SNIFFER MODEL TEMP
        SnifferModel.createBodyLayer().bakeRoot().render(poseStack, multiBufferSource.getBuffer(RenderType.LINES), i, j);
        poseStack.popPose();
    }
}
