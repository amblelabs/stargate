package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.v2.kernels.PegasusGateKernel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.RotationAxis;

public class GlyphRenderer {
    public float renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate, int light) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = gate.address().pos().getRotationDirection();
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.24f * multiplier : direction.getOffsetX() * 0.24f * multiplier;

        matrices.push();
        matrices.translate(0, -2.05f, 0);
        matrices.translate(xOffset, 0.05f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);

        GateState state = gate.state();

        int selectedIndex = state instanceof GateState.Closed closed ? closed.locked() : -1;
        float baseSpeed = 360f / Glyph.ALL.length; // degrees per glyph
        float time = MinecraftClient.getInstance().player.age / 200f;
        float rot = 0;
        boolean isDialing = state instanceof GateState.Closed closed && closed.isDialing();

        if (isDialing)
            rot = MathHelper.wrapDegrees(time * baseSpeed * Glyph.ALL.length);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));

        for (int i = 0; i < Glyph.ALL.length; i++) {
            boolean isInDial = state instanceof GateState.Closed closed && closed.contains(Glyph.ALL[i]);
            boolean isSelected = i == selectedIndex;

            int colour = gate.kernel() instanceof PegasusGateKernel ? 0xffffff : 0x17171b;

            if (isInDial) {
                colour = 0x17171b;
            }
            if (isSelected && isDialing) {
                colour = 0x17171b;
            }

            matrices.push();
            double angle = 2 * Math.PI * i / Glyph.ALL.length;
            matrices.translate(Math.sin(angle) * 117, Math.cos(angle) * 117, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));
            OrderedText text = Address.asText(String.valueOf(Glyph.ALL[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, isSelected ? 0xf000f0 : light);
            matrices.pop();
        }
        matrices.pop();
        return !isDialing ? 0 : (float) MathHelper.wrapDegrees(time * (Math.PI * 2 / Glyph.ALL.length) * Glyph.ALL.length);
    }
}