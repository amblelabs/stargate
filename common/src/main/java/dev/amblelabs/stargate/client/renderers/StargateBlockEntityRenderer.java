package dev.amblelabs.stargate.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {

    private ModelPart model;

    public StargateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        model = SnifferModel.createBodyLayer().bakeRoot();
    }

    @Override
    public void render(StargateBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        poseStack.pushPose();
        poseStack.scale(1, -1, -1);
        poseStack.translate(0.5, -1.5, 0.5);
        // SNIFFER MODEL TEMP
        this.model.render(poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/sniffer/sniffer.png"))), i, j);
        poseStack.popPose();
    }
}
