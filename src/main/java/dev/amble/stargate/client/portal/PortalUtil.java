package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PortalUtil {
    public Identifier TEXTURE_LOCATION;
    private final float scale;
    private float time = 0;
    private float radius = 0.08f;

    public PortalUtil(Identifier texture) {
        TEXTURE_LOCATION = texture;
        this.scale = 32f;
    }
    @ApiStatus.Internal
    public PortalUtil(String name) {
        this(StargateMod.id("textures/portal/" + name + ".png"));
    }

    public void renderPortalInterior(MatrixStack matrixStack, StargateBlockEntity stargate, Stargate.GateState state) {

        time += ((MinecraftClient.getInstance().player.age / 200f) * 100f); // Slow down the animation

        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);

        matrixStack.scale(scale, scale, scale);

        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE_LOCATION);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);

        RenderSystem.disableCull();
        portalTriangles(matrixStack, buffer, stargate, state);
        tessellator.draw();
        RenderSystem.enableCull();
        matrixStack.pop();
    }

    private void addVertexGlow(VertexConsumer builder, Matrix4f matrix, Matrix3f normalMatrix, float x, float y, float z, float u, float v, float r, float g, float b, float a, int light) {
        builder.vertex(matrix, x, y, z).color(Math.min(1, r), Math.min(1, g), Math.min(1, b), a).texture(u, v).light(light).next();
    }

    int glow(float x, float y) {
        float dist = (float)Math.sqrt(x * x + y * y) / radius;
        return (int) Math.max(0f, (float)Math.pow(1.0f - dist, 6));
    }


    public void portalTriangles(MatrixStack matrixStack, BufferBuilder buffer, StargateBlockEntity stargate, Stargate.GateState state) {
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90f));
        int sides = 18;
        int rings =24;
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        float minWaveHeight = 0.001f;
        float maxWaveHeight = stargate.getCachedState().get(StargateBlock.IRIS) ?  0.001f : 0.006f;

        int rippleCount = 12;
        float[][] rippleCenters = new float[rippleCount][2];
        float[] rippleRadii = new float[rippleCount];
        float[] rippleHeights = new float[rippleCount];
        for (int i = 0; i < rippleCount; i++) {
            float angle = (float) (2 * Math.PI * i / rippleCount + i * 0.7f);
            float dist = 0.03f + 0.03f * (i % 2);
            rippleCenters[i][0] = (float) Math.cos(angle) * dist;
            rippleCenters[i][1] = (float) Math.sin(angle) * dist;
            rippleRadii[i] = 0.03f + 0.02f * (i % 2);
            rippleHeights[i] = minWaveHeight + (maxWaveHeight - minWaveHeight) * ((i + 1) / (float) rippleCount);
        }

        float waveFrequency = 24f;
        float waveSpeed = 0.2f;

        // Add central big ripple if active

        if (state == Stargate.GateState.PREOPEN)
            triggerCentralRipple(0.055f, 0.175f, 0.01f, 0.2f);
        CentralRippleParams central = getCentralRipple();
        boolean hasCentralRipple = central != null;

        float[][][] mesh = new float[rings][sides][3];
        for (int r = 0; r < rings; r++) {
            float t = r / (float) (rings - 1);
            float ringRadius = radius * t;
            for (int i = 0; i < sides; i++) {
                float angle = (float) (2 * Math.PI * i / sides);
                float x = (float) Math.cos(angle) * ringRadius;
                float y = (float) Math.sin(angle) * ringRadius;
                float dx = x;
                float dy = y;
                float dz = 0f;
                // Ripples (skip center and outer ring)
                if (r != 0 && r != rings - 1) {
                    for (int rc = 0; rc < rippleCount; rc++) {
                        float cx = rippleCenters[rc][0];
                        float cy = rippleCenters[rc][1];
                        float distToCenter = (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                        float rippleRadius = rippleRadii[rc];
                        if (distToCenter < rippleRadius) {
                            float norm = 1f - (distToCenter / rippleRadius);
                            float phase = time * waveSpeed + rc * 1.3f;
                            dz += (float) (Math.sin(norm * waveFrequency + phase) * rippleHeights[rc] * norm * t);
                        }
                    }
                }

                // Central big ripple (affect all rings, but only near center)
                // Central big ripple (affect all rings, including center)
                if (hasCentralRipple) {
                    float distToCenter = (float) Math.sqrt(x * x + y * y);
                    if (distToCenter <= central.radius) {
                        float norm = 1f - (distToCenter / central.radius);
                        float bulge = (float) Math.pow(norm, 0.5f); // Skew the bulge towards the front
                        float phase = time * central.speed + central.phaseOffset;
                        dz += (float) (Math.sin(bulge * central.frequency + phase) * central.height * bulge * t);
                        // Move the center point with the ripple
                        dz += (float) (Math.sin(bulge * central.frequency + phase) * central.height * bulge);
                    }
                }
                mesh[r][i][0] = dx;
                mesh[r][i][1] = dy;
                mesh[r][i][2] = dz;
            }
        }

        for (int r = 0; r < rings - 1; r++) {
            for (int i = 0; i < sides; i++) {
                int next = (i + 1) % sides;
                float[] v0 = mesh[r][i];
                float[] v1 = mesh[r + 1][i];
                float[] v2 = mesh[r + 1][next];
                float u0 = 0.5f + (v0[0] / (2 * radius));
                float v0t = 0.5f + (v0[1] / (2 * radius));
                float u1 = 0.5f + (v1[0] / (2 * radius));
                float v1t = 0.5f + (v1[1] / (2 * radius));
                float u2 = 0.5f + (v2[0] / (2 * radius));
                float v2t = 0.5f + (v2[1] / (2 * radius));
                float maxHeight = 0.00175f;

                // Make the center area larger and more concentrated
                float centerRadius = radius * 0.9f; // larger center
                float coreRadius = radius * 0.25f;   // very concentrated core

                float dist0 = (float) Math.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                float dist1 = (float) Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1]);
                float dist2 = (float) Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);

                boolean centerTriangle = (dist0 < centerRadius) || (dist1 < centerRadius) || (dist2 < centerRadius);
                boolean coreTriangle = (dist0 < coreRadius) || (dist1 < coreRadius) || (dist2 < coreRadius);

                float intensity0 = Math.min(1f, Math.max(0.7f, (float) Math.pow(Math.max(0f, v0[2] / maxHeight), 2.5f)));
                float intensity1 = Math.min(1f, Math.max(0.7f, (float) Math.pow(Math.max(0f, v1[2] / maxHeight), 2.5f)));
                float intensity2 = Math.min(1f, Math.max(0.7f, (float) Math.pow(Math.max(0f, v2[2] / maxHeight), 2.5f)));

                if (coreTriangle) {
                    // Very center: strong, concentrated white/blue light
                    intensity0 = Math.min(1f, intensity0 + 1.0f);
                    intensity1 = Math.min(1f, intensity1 + 1.0f);
                    intensity2 = Math.min(1f, intensity2 + 1.0f);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, 1 * intensity0, 1 * intensity0, 1 *intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v1[0], v1[1], v1[2], u1, v1t, 1 * intensity1, 1 * intensity1, 1 *intensity1, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, 1 * intensity2, 1 * intensity2, 1 *intensity2, 1f, 0xf000f0);
                } else if (centerTriangle) {
                    // Larger center: blueish, softer, brightness based on distance from center
                    float centerDist0 = (float) Math.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                    float centerDist1 = (float) Math.sqrt(v1[0]*v1[0] + v1[1]*v1[1]);
                    float centerDist2 = (float) Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);
                    float centerNorm0 = 1f - Math.min(1f, centerDist0 / centerRadius);
                    float centerNorm1 = 1f - Math.min(1f, centerDist1 / centerRadius);
                    float centerNorm2 = 1f - Math.min(1f, centerDist2 / centerRadius);
                    intensity0 = Math.min(1f, intensity0 + 0.6f * centerNorm0);
                    intensity1 = Math.min(1f, intensity1 + 0.6f * centerNorm1);
                    intensity2 = Math.min(1f, intensity2 + 0.6f * centerNorm2);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, 1f * intensity0, 1f * intensity0, 1f * intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v1[0], v1[1], v1[2], u1, v1t, 1f * intensity1, 1f * intensity1, 1f * intensity1, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, 1f * intensity2, 1f * intensity2, 1f * intensity2, 1f, 0xf000f0);
                } else {
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, intensity0, intensity0, intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v1[0], v1[1], v1[2], u1, v1t, intensity1, intensity1, intensity1, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, intensity2, intensity2, intensity2, 1f, 0xf000f0);
                }

                float[] v3 = mesh[r][next];
                float u3 = 0.5f + (v3[0] / (2 * radius));
                float v3t = 0.5f + (v3[1] / (2 * radius));
                int glow3 = glow(v3[0], v3[1]);
                float intensity3 = Math.min(1f, Math.max(0.7f, (float) Math.pow(Math.max(0f, v3[2] / maxHeight), 2.5f)));

                float dist3 = (float) Math.sqrt(v3[0]*v3[0] + v3[1]*v3[1]);
                boolean centerTriangle2 = (dist0 < centerRadius) || (dist2 < centerRadius) || (dist3 < centerRadius);
                boolean coreTriangle2 = (dist0 < coreRadius) || (dist2 < coreRadius) || (dist3 < coreRadius);

                if (coreTriangle2) {
                    intensity0 = Math.min(1f, intensity0 + 1.0f);
                    intensity2 = Math.min(1f, intensity2 + 1.0f);
                    intensity3 = Math.min(1f, intensity3 + 1.0f);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, 1 * intensity0, 1 * intensity0,  intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, 1 * intensity2, 1 * intensity2,  intensity2, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v3[0], v3[1], v3[2], u3, v3t, 1 * intensity3, 1 * intensity3,  intensity3, 1f, 0xf000f0);
                } else if (centerTriangle2) {
                    // Larger center: blueish, softer, brightness based on distance from center
                    float centerDist0 = (float) Math.sqrt(v0[0]*v0[0] + v0[1]*v0[1]);
                    float centerDist2 = (float) Math.sqrt(v2[0]*v2[0] + v2[1]*v2[1]);
                    float centerDist3 = (float) Math.sqrt(v3[0]*v3[0] + v3[1]*v3[1]);
                    float centerNorm0 = 1f - Math.min(1f, centerDist0 / centerRadius);
                    float centerNorm2 = 1f - Math.min(1f, centerDist2 / centerRadius);
                    float centerNorm3 = 1f - Math.min(1f, centerDist3 / centerRadius);
                    intensity0 = Math.min(1f, intensity0 + 0.6f * centerNorm0);
                    intensity2 = Math.min(1f, intensity2 + 0.6f * centerNorm2);
                    intensity3 = Math.min(1f, intensity3 + 0.6f * centerNorm3);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, 1f * intensity0, 1f * intensity0, 1f * intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, 1f * intensity2, 1f * intensity2, 1f * intensity2, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v3[0], v3[1], v3[2], u3, v3t, 1f * intensity3, 1f * intensity3, 1f * intensity3, 1f, 0xf000f0);
                } else {
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v0[0], v0[1], v0[2], u0, v0t, intensity0, intensity0, intensity0, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v2[0], v2[1], v2[2], u2, v2t, intensity2, intensity2, intensity2, 1f, 0xf000f0);
                    addVertexGlow(buffer, matrix, matrixStack.peek().getNormalMatrix(), v3[0], v3[1], v3[2], u3, v3t, intensity3, intensity3, intensity3, 1f, 0xf000f0);
                }
            }
        }
    }

    /** Parameters for a central ripple. */
    private static class CentralRippleParams {
        float radius;
        float height;
        float frequency;
        float speed;
        float phaseOffset;

        CentralRippleParams(float radius, float height, float frequency, float speed, float phaseOffset) {
            this.radius = radius;
            this.height = height;
            this.frequency = frequency;
            this.speed = speed;
            this.phaseOffset = phaseOffset;
        }
    }

    private CentralRippleParams centralRipple = null;
    private float centralRippleTime = 0f;
    private boolean centralRippleSettling = false;

    /** Call this to trigger a big central ripple. */
    public void triggerCentralRipple(float radius, float height, float frequency, float speed) {
        this.centralRipple = new CentralRippleParams(radius, height, frequency, speed, 0f);
        this.centralRippleTime = 0f;
        this.centralRippleSettling = false;
    }

    /** Returns the current central ripple, or null if none. */
    private CentralRippleParams getCentralRipple() {
        if (centralRipple == null) return null;

        // Animation timing
        float mainDuration = 0.7f; // seconds for main ripple
        float settleDuration = 0.7f; // seconds for settle
        float dt = 1f / 20f; // assuming 20 TPS, adjust as needed

        centralRippleTime += dt;

        if (!centralRippleSettling && centralRippleTime > mainDuration) {
            // Start settling phase
            centralRippleSettling = true;
            centralRippleTime = 0f;
            // Water droplet effect: reduce height, increase frequency
            centralRipple.height *= 0.25f;
            centralRipple.frequency *= 2.5f;
            centralRipple.speed *= 1.5f;
        } else if (centralRippleSettling && centralRippleTime > settleDuration) {
            // End ripple
            centralRipple = null;
            centralRippleSettling = false;
            centralRippleTime = 0f;
            return null;
        }
        return centralRipple;
    }


}

