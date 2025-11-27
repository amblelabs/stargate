package dev.amble.stargate.client.renderers;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.block.entities.DialingComputerBlockEntity;
import dev.amble.stargate.client.models.DialingComputerModel;
import dev.amble.stargate.client.util.EmissionUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class DialingComputerRenderer implements BlockEntityRenderer<DialingComputerBlockEntity> {

    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/computer.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/computer_emission.png");

    private final DialingComputerModel model;

    public DialingComputerRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DialingComputerModel();
    }

    @Override
    public void render(DialingComputerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        EmissionUtil.render2Layers(model, TEXTURE, EMISSION, true, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }
}
