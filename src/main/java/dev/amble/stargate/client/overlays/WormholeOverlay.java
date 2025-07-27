package dev.amble.stargate.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.client.portal.WormholeRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;

public class WormholeOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        if (!MinecraftClient.getInstance().player.getMainHandStack().isOf(Items.DIAMOND_SWORD)) return;
        WormholeRenderer renderer = new WormholeRenderer("wormhole");
        MatrixStack stack = drawContext.getMatrices();
        for (int y = 0; y < MinecraftClient.getInstance().getWindow().getScaledHeight(); y++) {
            float t = (float) y / MinecraftClient.getInstance().getWindow().getScaledHeight();
            int r = (int) (20 + 80 * t);
            int g = (int) (20 + 10 * t);
            int b = (int) (40 + 120 * t);
            int color = (0xFF << 24) | (r << 16) | (g << 8) | b;
            drawContext.fill(
                    0, y,
                    MinecraftClient.getInstance().getWindow().getScaledWidth(), y + 1,
                    color
            );
        }
        stack.push();
        stack.translate(MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2, 5000);
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MinecraftClient.getInstance().player.age / 50f * 360f));
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) Math.sin(MinecraftClient.getInstance().player.age / 50f * 2f)));
        stack.scale(11f, 12f, 5.5f);
        RenderSystem.depthMask(false);
        renderer.renderWormhole(stack);
        RenderSystem.depthMask(true);
        //renderer.renderWormholeLayer(stack, 1.5f);
        //renderer.renderWormholeLayer(stack, 2.5f);
        stack.pop();
    }
}
