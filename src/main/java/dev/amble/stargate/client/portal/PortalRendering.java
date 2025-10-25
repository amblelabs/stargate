package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayDeque;
import java.util.Queue;

public class PortalRendering {
    public static final Queue<StargateBlockEntity> QUEUE = new ArrayDeque<>();

    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        MatrixStack stack = context.matrixStack();
        float time = client.player.age / 200f * 100f; // Slow down the animation

        while (!PortalRendering.QUEUE.isEmpty()) {
            StargateBlockEntity gate = PortalRendering.QUEUE.poll();

            if (gate == null) continue;

            Stargate stargate = gate.asGate();

            if (stargate == null)
                continue;

            ClientAbstractStargateState clientState = stargate.stateOrNull(ClientAbstractStargateState.state);

            if (clientState == null) continue;

            render(client, stack, gate, stargate, clientState, time);
        }
    }

    public static void render(MinecraftClient client, MatrixStack stack, StargateBlockEntity entity, Stargate stargate, ClientAbstractStargateState clientState, float age) {
        Vec3d pos = entity.getPos().toCenterPos()
                .subtract(client.gameRenderer.getCamera().getPos());

        stack.push();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation()));
        stack.translate(0, -2f, 0);

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));

        stack.translate(0, clientState.portalYOffset, 0);
        stack.scale(clientState.portalSize, clientState.portalSize, clientState.portalSize);

        GateState<?> state = stargate.getGateState();

        // can this be moved outside the loop?
        RenderSystem.enableDepthTest();

        renderPortalInterior(stack, clientState, state, age);

        RenderSystem.disableDepthTest();

        stack.pop();
        stack.pop();

        stack.pop();
    }

    static final float maxWaveHeight = 0.006f;
    static final float minWaveHeight = 0.001f;

    static final float waveFrequency = 24f;
    static final float waveSpeed = 0.2f;

    static final float radius = 0.08f;

    // Make the center area larger and more concentrated
    static final float centerRadius = radius * 0.9f; // larger center
    static final float coreRadius = radius * 0.25f;   // very concentrated core

    static final float centralRadius = 0.05f;
    static final float centralHeight = 0.6f;
    static final float centralFreq = 0.01f;
    static final float centralSpeed = 0.2f;
    static final float centralOffset = 0f;

    static final float maxHeight = 0.00175f;
    static final int frames = 18; // Number of animation frames (vertical frames);

    static final int sides = 18;
    static final int rings = 36;

    static final int rippleCount = 12;
    static final float[][] rippleCenters = new float[rippleCount][2];
    static final float[] rippleRadii = new float[rippleCount];
    static final float[] rippleHeights = new float[rippleCount];

    static {
        for (int i = 0; i < rippleCount; i++) {
            final float angle = 2 * MathHelper.PI * i / rippleCount + i * 0.7f;
            final float dist = 0.03f + 0.03f * (i % 2);
            rippleCenters[i][0] = MathHelper.cos(angle) * dist;
            rippleCenters[i][1] = MathHelper.sin(angle) * dist;
            rippleRadii[i] = 0.03f + 0.02f * (i % 2);
            rippleHeights[i] = minWaveHeight + (maxWaveHeight - minWaveHeight) * ((i + 1) / (float) rippleCount);
        }
    }

    private static void renderPortalInterior(MatrixStack matrixStack, ClientAbstractStargateState clientState, GateState<?> currentState, float time) {
        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);

        Identifier texture = clientState.portalType;
        RenderSystem.setShaderTexture(0, texture);
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        RenderSystem.disableCull();

        portalTriangles(matrixStack, buffer, currentState, time);
        tessellator.draw();

        RenderSystem.enableCull();
        matrixStack.pop();
    }

    private static void addVertexGlow(VertexConsumer builder, Matrix4f matrix, float x, float y, float z, float u, float v, float r) {
        r = Math.min(1, r);
        builder.vertex(matrix, x, y, z).color(r, r, r, 1f).texture(u, v).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
    }

    private static void portalTriangles(MatrixStack matrixStack, VertexConsumer buffer, GateState<?> currentState, float time) {
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90f));

        final Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        // Add central big ripple if active
        final boolean hasCentral = currentState.gateState() == GateState.StateType.OPENING;
        final float[][][] mesh = new float[rings][sides][3];

        for (int r = 0; r < rings; r++) {
            final float t = r / (float) (rings - 1);
            final float ringRadius = radius * t;
            for (int i = 0; i < sides; i++) {
                final float angle = 2 * MathHelper.PI * i / sides;
                final float x = MathHelper.cos(angle) * ringRadius;
                final float y = MathHelper.sin(angle) * ringRadius;
                float dz = 0f;

                // Ripples (skip center and outer ring)
                if (r != 0 && r != rings - 1) {
                    for (int rc = 0; rc < rippleCount; rc++) {
                        final float cx = rippleCenters[rc][0];
                        final float cy = rippleCenters[rc][1];
                        final float distToCenter = MathHelper.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                        final float rippleRadius = rippleRadii[rc];
                        if (distToCenter < rippleRadius) {
                            final float norm = 1f - (distToCenter / rippleRadius);
                            final float phase = time * waveSpeed + rc * 1.3f;
                            dz += MathHelper.sin(norm * waveFrequency + phase) * rippleHeights[rc] * norm * t;
                        }
                    }
                }

                // Central big ripple (affect all rings, but only near center)
                if (hasCentral) {
                    final float distToCenter = MathHelper.sqrt(x * x + y * y);
                    if (distToCenter <= centralRadius) {
                        final float norm = 1f - (distToCenter / centralRadius);
                        final float wave = getWave(currentState, angle, norm, time);
                        float effect;
                        if (wave >= 0f) {
                            effect = wave * centralHeight * norm * t;
                        } else {
                            // Soften the backward (negative) effect
                            effect = wave * 0.25f * centralHeight * norm * t;
                        }
                        // Suppress the "needle" in the very center
                        if (distToCenter < centralRadius * 0.12f) {
                            effect = 0f;
                        }
                        dz += effect;
                        dz += wave * centralHeight * norm * 0.2f; // minimal backward movement, mostly forward
                    }
                }
                mesh[r][i][0] = x;
                mesh[r][i][1] = y;
                mesh[r][i][2] = dz;
            }
        }

        for (int r = 0; r < rings - 1; r++) {
            for (int i = 0; i < sides; i++) {
                final int next = (i + 1) % sides;
                final int currentFrame = (int) (time % frames);

                final float[] v0 = mesh[r][i];
                final float[] v1 = mesh[r + 1][i];
                final float[] v2 = mesh[r + 1][next];
                final float[] v3 = mesh[r][next];

                final float u0 = (v0[0] / (2 * radius) + 0.5f);
                final float u1 = (v1[0] / (2 * radius) + 0.5f);
                final float u2 = (v2[0] / (2 * radius) + 0.5f);
                final float u3 = (v3[0] / (2 * radius) + 0.5f);

                final float v0t = (v0[1] / (2 * radius) + 0.5f) / frames + (float) currentFrame / frames;
                final float v1t = (v1[1] / (2 * radius) + 0.5f) / frames + (float) currentFrame / frames;
                final float v2t = (v2[1] / (2 * radius) + 0.5f) / frames + (float) currentFrame / frames;
                final float v3t = (v3[1] / (2 * radius) + 0.5f) / frames + (float) currentFrame / frames;

                final float dist0 = MathHelper.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                final float dist1 = MathHelper.sqrt(v1[0]*v1[0] + v1[1]*v1[1]);
                final float dist2 = MathHelper.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);
                final float dist3 = MathHelper.sqrt(v3[0]*v3[0] + v3[1]*v3[1]);

                final boolean centerTriangle = (dist0 < centerRadius) || (dist1 < centerRadius) || (dist2 < centerRadius);
                final boolean coreTriangle = (dist0 < coreRadius) || (dist1 < coreRadius) || (dist2 < coreRadius);

                final boolean centerTriangle2 = (dist0 < centerRadius) || (dist2 < centerRadius) || (dist3 < centerRadius);
                final boolean coreTriangle2 = (dist0 < coreRadius) || (dist2 < coreRadius) || (dist3 < coreRadius);

                float intensity0 = MathHelper.clamp((float) Math.pow(Math.max(0f, v0[2] / maxHeight), 2.5f), 0.7f, 1f);
                float intensity1 = MathHelper.clamp((float) Math.pow(Math.max(0f, v1[2] / maxHeight), 2.5f), 0.7f, 1f);
                float intensity2 = MathHelper.clamp((float) Math.pow(Math.max(0f, v2[2] / maxHeight), 2.5f), 0.7f, 1f);
                float intensity3 = MathHelper.clamp((float) Math.pow(Math.max(0f, v3[2] / maxHeight), 2.5f), 0.7f, 1f);

                if (coreTriangle) {
                    // Very center: strong, concentrated white/blue light
                    intensity0 = Math.min(1f, intensity0 + 1);
                    intensity1 = Math.min(1f, intensity1 + 1);
                    intensity2 = Math.min(1f, intensity2 + 1);
                } else if (centerTriangle) {
                    // Larger center: blueish, softer, brightness based on distance from center
                    final float centerDist0 = (float) Math.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                    final float centerDist1 = (float) Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1]);
                    final float centerDist2 = (float) Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);

                    final float centerNorm0 = 1f - Math.min(1f, centerDist0 / centerRadius);
                    final float centerNorm1 = 1f - Math.min(1f, centerDist1 / centerRadius);
                    final float centerNorm2 = 1f - Math.min(1f, centerDist2 / centerRadius);

                    intensity0 = Math.min(1f, intensity0 + 0.6f * centerNorm0);
                    intensity1 = Math.min(1f, intensity1 + 0.6f * centerNorm1);
                    intensity2 = Math.min(1f, intensity2 + 0.6f * centerNorm2);
                }

                addVertexGlow(buffer, matrix, v0[0], v0[1], v0[2], u0, v0t, intensity0);
                addVertexGlow(buffer, matrix, v1[0], v1[1], v1[2], u1, v1t, intensity1);
                addVertexGlow(buffer, matrix, v2[0], v2[1], v2[2], u2, v2t, intensity2);

                if (coreTriangle2) {
                    intensity0 = Math.min(1f, intensity0 + 1);
                    intensity2 = Math.min(1f, intensity2 + 1);
                    intensity3 = Math.min(1f, intensity3 + 1);
                } else if (centerTriangle2) {
                    // Larger center: blueish, softer, brightness based on distance from center
                    final float centerDist0 = (float) Math.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                    final float centerDist2 = (float) Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);
                    final float centerDist3 = (float) Math.sqrt(v3[0]*v3[0] + v3[1]*v3[1]);

                    final float centerNorm0 = 1f - Math.min(1f, centerDist0 / centerRadius);
                    final float centerNorm2 = 1f - Math.min(1f, centerDist2 / centerRadius);
                    final float centerNorm3 = 1f - Math.min(1f, centerDist3 / centerRadius);

                    intensity0 = Math.min(1f, intensity0 + 0.6f * centerNorm0);
                    intensity2 = Math.min(1f, intensity2 + 0.6f * centerNorm2);
                    intensity3 = Math.min(1f, intensity3 + 0.6f * centerNorm3);
                }

                addVertexGlow(buffer, matrix, v0[0], v0[1], v0[2], u0, v0t, intensity0);
                addVertexGlow(buffer, matrix, v2[0], v2[1], v2[2], u2, v2t, intensity2);
                addVertexGlow(buffer, matrix, v3[0], v3[1], v3[2], u3, v3t, intensity3);
            }
        }
    }

    private static float getWave(GateState<?> currentState, float angle, float bulge, float time) {
        float realHeight = currentState instanceof GateState.Opening opening ? opening.kawooshHeight : 0;

        float phase = 1.25f * realHeight * centralSpeed + centralOffset;
        float twist = MathHelper.sin(time * 0.7f + angle * 2.5f) * 0.1f;
        return MathHelper.sin(bulge * centralFreq + phase + twist);
    }

}
