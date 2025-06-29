package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Dialer;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.client.models.DHDModel;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.entities.DHDBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class DHDBlockEntityRenderer implements BlockEntityRenderer<DHDBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd_emission.png");
    private final DHDModel model;
    public DHDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DHDModel(DHDModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(DHDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        if (!entity.hasStargate()) {
            matrices.pop();
            return;
        }

        Dialer dialer = entity.getStargate().get().getDialer();

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int middleIndex = Glyph.ALL.length / 2;
        for (int i = 0; i < Glyph.ALL.length; i++) {
            int j = Glyph.ALL.length - i + dialer.getSelectedIndex() - middleIndex;

            if (j < 0) {
                j += Glyph.ALL.length;
            } else if (j >= Glyph.ALL.length) {
                j -= Glyph.ALL.length;
            }

            boolean isInDial = dialer.contains(Glyph.ALL[i]);
            boolean isSelected = i == dialer.getSelectedIndex();

            int colour = 0x4f4f4f;

            if (isInDial) {
                colour = 0xedc093;
            }
            if (isSelected) {
                colour = 0xedb334;
            }

            matrices.push();
            double angle = 2 * Math.PI * j / Glyph.ALL.length;
            matrices.translate(Math.sin(angle) * 20f, Math.cos(angle) * 20f, 0);
            OrderedText text = Address.asText(String.valueOf(Glyph.ALL[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, 0, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 0xF000F0);
            matrices.pop();
        }
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}