package dev.amblelabs.stargate.client.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.behavior.GateManagerBehavior;
import dev.amblelabs.stargate.common.impl.ecs.behavior.KawooshBehavior;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.drtheo.ecs.behavior.Resolve;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import software.bernie.geckolib.util.Color;

public class ClientPuddleBehavior implements TBehavior, StargateBlockEvents.Tick {

    private static final float MAX_RADIUS = KawooshBehavior.MAX_RADIUS;
    private static final float INNER_WHITE_RADIUS = 0.8f;

    private static final int INNER_BG_COLOR = Color.ofRGB(0.85f, 0.85f, 1.0f).getColor();
    private static final int OUTER_BG_COLOR = Color.ofRGB(0.05f, 0.75f, 1.0f).getColor();

    private static final int INNER_RIPPLE_COLOR = Color.WHITE.getColor();
    private static final int OUTER_RIPPLE_COLOR = Color.ofRGB(0.2f, 0.65f, 1.0f).getColor();

    private static final float KAWOOSH_MAX_DISTANCE = KawooshBehavior.KAWOOSH_MAX_DISTANCE;
    private static final float KAWOOSH_BULB_RADIUS = MAX_RADIUS / 2f;
    public static final float JET_STEM = 0.75f;

    public static final int KAWOOSH_CONVERGE_TICKS = 10;
    private static final int KAWOOSH_JET_OUT_TICKS = 8;
    private static final int KAWOOSH_JET_HOLD_TICKS = 3;
    private static final int KAWOOSH_FALL_TICKS = 13;
    private static final int KAWOOSH_TOTAL_TICKS =
            KAWOOSH_CONVERGE_TICKS + KAWOOSH_JET_OUT_TICKS + KAWOOSH_JET_HOLD_TICKS + KAWOOSH_FALL_TICKS;

    private static final int KAWOOSH_CORE_COLOR = Color.ofRGB(0.97f, 0.99f, 1.0f).getColor();
    private static final int KAWOOSH_EDGE_COLOR = Color.ofRGB(0.75f, 0.88f, 1.0f).getColor();

    private static final Minecraft mc = Minecraft.getInstance();

    @Resolve
    private final GateManagerBehavior manager = behavior();

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide() || mc.player == null) return;

        GateState<?> gateState = this.manager.getCurrent(stargate);
        if (gateState instanceof GateState.Closed)
            return;

        Direction facing = blockState.getValue(StargateBlock.FACING);

        Vec3 localX = null;
        Vec3 localY = null;

        int sign = facing.getAxisDirection().getStep(); // returns +1 or -1

        switch (facing.getAxis()) {
            case X -> {
                localX = new Vec3(0, 0, -sign);
                localY = new Vec3(0, 1, 0);
            }
            case Z -> {
                localX = new Vec3(sign, 0, 0);
                localY = new Vec3(0, 1, 0);
            }
            case Y -> {
                localX = new Vec3(1, 0, 0);
                localY = new Vec3(0, 0, sign);
            }
        }

        Vec3 center = blockPos.above(3).getCenter();
        long gameTime = level.getGameTime();

        if (gateState instanceof GateState.Opening opening) {
            // Out the front of the gate.
            Vec3 normal = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
            spawnKawoosh(level, center, normal, localX, localY, opening.timer);
        }

        int targetTick = StargateConfig.client().puddleParticleTick();
        if (targetTick == 0 || mc.player.tickCount % targetTick != 0) return;

        int cycleTicks = StargateConfig.client().puddleCycleTicks();
        float progress = (gameTime % cycleTicks) / (float) cycleTicks;
        float currentRadius = progress * MAX_RADIUS;

        int ringParticleCount = Math.max((int) (currentRadius * 20 + 12), 20);
        int bgColor = currentRadius <= INNER_WHITE_RADIUS ? INNER_BG_COLOR : OUTER_BG_COLOR;

        if (currentRadius < 0.5f) {
            int centerParticles = 1;
            for (int i = 0; i < centerParticles; i++) {
                float angle = (float) (Math.random() * 2 * Mth.PI);
                float radius = (float) (Math.random() * 0.2f);

                float offsetX = Mth.cos(angle) * radius;
                float offsetY = Mth.sin(angle) * radius;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                double worldX = center.x + (offsetX * localX.x) + (offsetY * localY.x);
                double worldY = center.y + (offsetX * localX.y) + (offsetY * localY.y);
                double worldZ = center.z + (offsetX * localX.z) + (offsetY * localY.z);

                double velX = Mth.cos(angle) * 0.2;
                double velY = Mth.sin(angle) * 0.2;
                double worldVelX = (velX * localX.x) + (velY * localY.x);
                double worldVelY = (velX * localX.y) + (velY * localY.y);
                double worldVelZ = (velX * localX.z) + (velY * localY.z);

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, uv),
                        worldX, worldY, worldZ,
                        worldVelX, worldVelY, worldVelZ
                );
            }
        }

        for (int i = 0; i < ringParticleCount; i++) {
            float angle = (2 * Mth.PI * i) / ringParticleCount;
            float shiftedAngle = angle + (gameTime * 0.2f);

            float jitter = 0.05f;
            float radiusJitter = currentRadius + (float)(Math.random() - 0.5) * jitter;
            float angleJitter = (float)(Math.random() - 0.5) * 0.1f;
            float finalAngle = shiftedAngle + angleJitter;

            float offsetX = Mth.cos(finalAngle) * radiusJitter;
            float offsetY = Mth.sin(finalAngle) * radiusJitter;
            Vector2f uv = toEventHorizonUv(offsetX, offsetY);

            float speed = 0.3f + (currentRadius / MAX_RADIUS) * 0.3f;
            double velX = Mth.cos(finalAngle) * speed;
            double velY = Mth.sin(finalAngle) * speed;

            double worldVelX = (velX * localX.x) + (velY * localY.x);
            double worldVelY = (velX * localX.y) + (velY * localY.y);
            double worldVelZ = (velX * localX.z) + (velY * localY.z);

            double worldX = center.x + (offsetX * localX.x) + (offsetY * localY.x);
            double worldY = center.y + (offsetX * localX.y) + (offsetY * localY.y);
            double worldZ = center.z + (offsetX * localX.z) + (offsetY * localY.z);

            level.addAlwaysVisibleParticle(
                    new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, uv),
                    worldX, worldY, worldZ,
                    worldVelX, worldVelY, worldVelZ
            );
        }

        float rippleRadius = Math.max(0, currentRadius - 0.8f);
        if (rippleRadius > 0.1f) {
            int rippleParticleCount = Math.max((int) (rippleRadius * 16 + 8), 12);
            int rippleColor = rippleRadius <= INNER_WHITE_RADIUS ? INNER_RIPPLE_COLOR : OUTER_RIPPLE_COLOR;

            for (int i = 0; i < rippleParticleCount; i++) {
                float angle = (2 * Mth.PI * i) / rippleParticleCount;
                float shiftedAngle = angle + (gameTime * 0.15f);

                float offsetX = Mth.cos(shiftedAngle) * rippleRadius;
                float offsetY = Mth.sin(shiftedAngle) * rippleRadius;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                // Slower outward velocity for ripple
                float speed = 0.2f + (rippleRadius / MAX_RADIUS) * 0.2f;
                double velX = Mth.cos(shiftedAngle) * speed;
                double velY = Mth.sin(shiftedAngle) * speed;

                double worldVelX = (velX * localX.x) + (velY * localY.x);
                double worldVelY = (velX * localX.y) + (velY * localY.y);
                double worldVelZ = (velX * localX.z) + (velY * localY.z);

                double worldX = center.x + (offsetX * localX.x) + (offsetY * localY.x);
                double worldY = center.y + (offsetX * localX.y) + (offsetY * localY.y);
                double worldZ = center.z + (offsetX * localX.z) + (offsetY * localY.z);

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, rippleColor, uv),
                        worldX, worldY, worldZ,
                        worldVelX, worldVelY, worldVelZ
                );
            }
        }
    }

    private static float easeOut(float t) {
        t = Mth.clamp(t, 0f, 1f);
        float inv = 1f - t;
        return 1f - inv * inv * inv;
    }

    private static float easeIn(float t) {
        t = Mth.clamp(t, 0f, 1f);
        return t * t * t;
    }

    private static float jetReach(long age) {
        long j = age - KAWOOSH_CONVERGE_TICKS;
        if (j < 0) return 0f;

        if (j < KAWOOSH_JET_OUT_TICKS)
            return easeOut(j / (float) KAWOOSH_JET_OUT_TICKS);

        if (j < KAWOOSH_JET_OUT_TICKS + KAWOOSH_JET_HOLD_TICKS)
            return 1f;

        long f = j - KAWOOSH_JET_OUT_TICKS - KAWOOSH_JET_HOLD_TICKS;
        if (f < KAWOOSH_FALL_TICKS)
            return 1f - easeIn(f / (float) KAWOOSH_FALL_TICKS);

        return 0f;
    }

    private static float jetProfile(float p) {
        float x = (p - 0.80f) / 0.20f;
        float bulb = Mth.sqrt(Mth.clamp(1f - x * x, 0f, 1f));
        return Math.max(JET_STEM, bulb);
    }

    private void spawnKawoosh(Level level, Vec3 center, Vec3 normal, Vec3 localX, Vec3 localY, long age) {
        if (age < 0 || age >= KAWOOSH_TOTAL_TICKS) return;

        long gameTime = level.getGameTime();

        spawnSurface(level, center, localX, localY, age, gameTime);

        float reach = jetReach(age);
        if (reach > 0.001f)
            spawnJet(level, center, normal, localX, localY, reach, age, gameTime);
    }

    private void spawnSurface(Level level, Vec3 center, Vec3 localX, Vec3 localY, long age, long gameTime) {
        boolean forming = age < KAWOOSH_CONVERGE_TICKS;
        float hole = forming ? MAX_RADIUS * (1f - age / (float) KAWOOSH_CONVERGE_TICKS) : 0f;

        for (float r = MAX_RADIUS; r >= hole; r -= 0.4f) {
            if (r < 0.06f) continue;

            int count = Math.max((int) (r * 10f + 5f), 8);
            boolean leadingEdge = forming && (r - hole) < 0.4f;
            int color = leadingEdge ? KAWOOSH_CORE_COLOR : KAWOOSH_EDGE_COLOR;

            for (int i = 0; i < count; i++) {
                float angle = (2 * Mth.PI * i) / count + gameTime * 0.08f
                        + (float) (Math.random() - 0.5) * 0.25f;
                float rr = r + (float) (Math.random() - 0.5) * 0.18f;

                float offsetX = Mth.cos(angle) * rr;
                float offsetY = Mth.sin(angle) * rr;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                Vec3 pos = center.add(localX.scale(offsetX)).add(localY.scale(offsetY));

                float inSpeed = forming ? 0.05f : 0.0f;
                Vec3 vel = localX.scale(-Mth.cos(angle) * inSpeed)
                        .add(localY.scale(-Mth.sin(angle) * inSpeed));

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.KAWOOSH, color, uv),
                        pos.x, pos.y, pos.z,
                        vel.x, vel.y, vel.z
                );
            }
        }
    }

    private void spawnJet(Level level, Vec3 center, Vec3 normal, Vec3 localX, Vec3 localY, float reach, long age, long gameTime) {
        float distance = KAWOOSH_MAX_DISTANCE * reach;
        int layerCount = Math.max((int) (distance * 4f), 6);

        for (int layer = 0; layer <= layerCount; layer++) {
            float p = layer / (float) layerCount;
            float depth = distance * p;
            float radius = KAWOOSH_BULB_RADIUS * jetProfile(p);

            float phase = gameTime * 0.5f + p * 5f;
            float sway = 0.12f * KAWOOSH_BULB_RADIUS * p;
            Vec3 layerCenter = center
                    .add(normal.scale(depth))
                    .add(localX.scale(Mth.cos(phase) * sway))
                    .add(localY.scale(Mth.sin(phase * 1.3f) * sway));

            int particlesInLayer = Math.max((int) (radius * 20f + 4f), 5);

            for (int i = 0; i < particlesInLayer; i++) {
                float angle = (2 * Mth.PI * i) / particlesInLayer + gameTime * 0.15f
                        + (float) (Math.random() - 0.5) * 0.25f;
                float rr = radius + (float) (Math.random() - 0.5) * 0.22f * KAWOOSH_BULB_RADIUS;

                float offsetX = Mth.cos(angle) * rr;
                float offsetY = Mth.sin(angle) * rr;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                Vec3 pos = layerCenter.add(localX.scale(offsetX)).add(localY.scale(offsetY));

                float churn = 0.02f;
                Vec3 vel = localX.scale(Mth.cos(angle) * churn)
                        .add(localY.scale(Mth.sin(angle) * churn));

                int color = p > 0.6f ? KAWOOSH_EDGE_COLOR : KAWOOSH_CORE_COLOR;
                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.KAWOOSH, color, uv),
                        pos.x, pos.y, pos.z,
                        vel.x, vel.y, vel.z
                );
            }
        }

        long j = age - KAWOOSH_CONVERGE_TICKS;
        if (j < KAWOOSH_JET_OUT_TICKS + KAWOOSH_JET_HOLD_TICKS) {
            Vec3 bulbCenter = center.add(normal.scale(distance * 0.85f));

            for (int i = 0; i < 8; i++) {
                float angle = (float) (Math.random() * 2 * Mth.PI);
                float rr = KAWOOSH_BULB_RADIUS * (0.5f + (float) Math.random() * 0.5f);

                float offsetX = Mth.cos(angle) * rr;
                float offsetY = Mth.sin(angle) * rr;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                Vec3 pos = bulbCenter.add(localX.scale(offsetX)).add(localY.scale(offsetY));

                float outSpeed = 0.08f + (float) Math.random() * 0.10f;
                float radialSpeed = 0.04f + (float) Math.random() * 0.05f;
                Vec3 vel = normal.scale(outSpeed)
                        .add(localX.scale(Mth.cos(angle) * radialSpeed))
                        .add(localY.scale(Mth.sin(angle) * radialSpeed));

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.KAWOOSH, KAWOOSH_EDGE_COLOR, uv),
                        pos.x, pos.y, pos.z,
                        vel.x, vel.y, vel.z
                );
            }
        }
    }

    private static Vector2f toEventHorizonUv(float offsetX, float offsetY) {
        float u = (float) (offsetX / (MAX_RADIUS * 2.0) + 0.5);
        float v = (float) (offsetY / (MAX_RADIUS * 2.0) + 0.5);
        return new Vector2f(Mth.clamp(u, 0, 1), Mth.clamp(1 - v, 0, 1));
    }
}