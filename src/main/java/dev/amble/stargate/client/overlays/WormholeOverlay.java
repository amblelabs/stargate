package dev.amble.stargate.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.client.portal.WormholeRenderer;
import dev.amble.stargate.client.renderers.SpaceSkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class WormholeOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        if (!MinecraftClient.getInstance().player.getMainHandStack().isOf(Items.DIAMOND_SWORD)) return;
        WormholeRenderer renderer = new WormholeRenderer("wormhole");
        MatrixStack stack = drawContext.getMatrices();
        Matrix4f projectionMatrix = MinecraftClient.getInstance().gameRenderer.getBasicProjectionMatrix(MinecraftClient.getInstance().options.getFov().getValue());
        /*for (int y = 0; y < MinecraftClient.getInstance().getWindow().getScaledHeight(); y++) {
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
        }*/
        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-405f));
        stack.scale(100, 100, 100);
        renderSpace(stack/*, () -> BackgroundRenderer.setFogBlack(), MinecraftClient.getInstance().worldRenderer.getStarsBuffer()*/, MinecraftClient.getInstance().world, v, projectionMatrix);
        stack.pop();
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

    public static void renderSpace(MatrixStack matrices/*, Runnable fogCallback, VertexBuffer starsBuffer*/, ClientWorld world, float tickDelta, Matrix4f projectionMatrix) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-405f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getSkyAngle(tickDelta) * 360.0f + 300f));
        matrices.scale(100, 100, 100);

        drawSpace(tessellator, bufferBuilder, matrices);

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getSkyAngle(tickDelta) * 360.0f));

        RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);
        BackgroundRenderer.clearFog();

        /*starsBuffer.bind();
        starsBuffer.draw(matrices.peek().getPositionMatrix(), projectionMatrix,
                GameRenderer.getPositionProgram());*/

        VertexBuffer.unbind();
        //fogCallback.run();

        RenderSystem.depthMask(false);
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        matrices.pop();
        matrices.pop();

        // Planet Rendering
        /*Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        matrices.push();
        renderStarBody(false, matrices, SUN,
                new Vec3d(cameraPos.getX() + 270, cameraPos.getY() - 120, cameraPos.getZ() + 0), new
                        Vector3f(12f, 12f, 12f),
                new Vector3f(12, 45, 0),
                true,
                new Vector3f(0.3f, 0.15f, 0.01f));

        renderSkyBody(tallDrinkOfWater + (Direction.fromRotation(tallDrinkOfWater).equals(Direction.WEST) || Direction.fromRotation(tallDrinkOfWater).equals(Direction.EAST) ? -90f : 90f), false, matrices, AITMod.id("textures/environment/earth.png"),
                new Vec3d(cameraPos.getX() - 530, cameraPos.getY() + 40, cameraPos.getZ() + 10), new
                        Vector3f(76f, 76f, 76f),
                new Vector3f(-22.5f, 45f, 0), true, true,
                new Vector3f(0.18f, 0.35f, 0.60f));
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(GL11.GL_LESS);
        matrices.pop();*/
    }

    private static void drawSpace(Tessellator tessellator, BufferBuilder bufferBuilder, MatrixStack matrices) {
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1);
        SpaceSkyRenderer cubeMap = new SpaceSkyRenderer(StargateMod.id("textures/environment/space_sky/panorama"));
        cubeMap.draw(tessellator, bufferBuilder, matrices);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1);
    }
}
