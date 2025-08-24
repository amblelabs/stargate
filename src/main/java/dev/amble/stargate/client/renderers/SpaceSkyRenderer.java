package dev.amble.stargate.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SpaceSkyRenderer {
    public static final Quaternionf[] LOOKUP = new Quaternionf[]{null, RotationAxis.POSITIVE_X.rotationDegrees(90.0f),
            RotationAxis.POSITIVE_X.rotationDegrees(-90.0f), RotationAxis.POSITIVE_X.rotationDegrees(180.0f),
            RotationAxis.POSITIVE_Z.rotationDegrees(90.0f), RotationAxis.POSITIVE_Z.rotationDegrees(-90.0f), null};
    private static final int FACES_COUNT = 6;
    private final Identifier[] faces = new Identifier[6];

    public SpaceSkyRenderer(Identifier faces) {
        for (int i = 0; i < 6; ++i) {
            this.faces[i] = faces.withPath(faces.getPath() + "_" + i + ".png");
        }
    }

    public void draw(Tessellator tessellator, BufferBuilder bufferBuilder, MatrixStack matrixStack) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();

        for (int k = 0; k < 6; ++k) {
            matrixStack.push();
            Quaternionf rot = LOOKUP[k];

            if (rot != null) {
                matrixStack.multiply(rot);
            }

            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

            RenderSystem.setShaderTexture(0, this.faces[k]);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            int l = 255;
            bufferBuilder.vertex(matrix4f,-100.0f, -100.0f, -100.0f).texture(0.0f, 0.0f).color(255, 255, 255, l).next();
            bufferBuilder.vertex(matrix4f,-100.0f, -100.0f, 100.0f).texture(0.0f, 1.0f).color(255, 255, 255, l).next();
            bufferBuilder.vertex(matrix4f,100.0f, -100.0f, 100.0f).texture(1.0f, 1.0f).color(255, 255, 255, l).next();
            bufferBuilder.vertex(matrix4f,100.0f, -100.0f, -100.0f).texture(1.0f, 0.0f).color(255, 255, 255, l).next();
            tessellator.draw();
            matrixStack.pop();
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
