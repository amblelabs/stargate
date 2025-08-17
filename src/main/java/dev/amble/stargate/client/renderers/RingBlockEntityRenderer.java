package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.block.DHDBlock;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.block.entities.StargateRingBlockEntity;
import dev.amble.stargate.client.models.DHDModel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.*;

public class RingBlockEntityRenderer implements BlockEntityRenderer<StargateRingBlockEntity> {

    public RingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(StargateRingBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getBlockSet() != null) {
            BlockState blockState = entity.getBlockSet();
            matrices.push();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(blockState, entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(blockState)), true, MinecraftClient.getInstance().world.getRandom());
            matrices.pop();
        }
    }

}