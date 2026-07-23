package dev.amblelabs.stargate.client.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.Color;

public class ClientPuddleBehavior implements TBehavior, StargateBlockEvents {

    private static final float MAX_RADIUS = 2.6f;
    private static final float INNER_WHITE_RADIUS = 0.8f;

    private static final int INNER_BG_COLOR = Color.ofRGB(0.85f, 0.85f, 1.0f).getColor();
    private static final int OUTER_BG_COLOR = Color.ofRGB(0.05f, 0.75f, 1.0f).getColor();

    private static final int INNER_RIPPLE_COLOR = Color.WHITE.getColor();
    private static final int OUTER_RIPPLE_COLOR = Color.ofRGB(0.2f, 0.65f, 1.0f).getColor();

    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState oldState, boolean movedByPiston) { }

    @Override
    public void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston) { }

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide() || mc.player == null) return;

        if (stargate.state(GateState.state) instanceof GateState.Closed)
            return;

        int targetTick = StargateConfig.client().puddleParticleTick();
        if (targetTick == 0 || mc.player.tickCount % StargateConfig.client().puddleParticleTick() != 0) return;

        Direction facing = blockState.getValue(StargateBlock.FACING);

        Vec3 localX = null;
        Vec3 localY = null;

        int sign = facing.getAxisDirection().getStep(); // returns +1 or -1

        switch (facing.getAxis()) {
            case X -> {
                localX = new Vec3(0, 0, -sign);  // WEST -> +Z, EAST -> -Z
                localY = new Vec3(0, 1, 0);
            }
            case Z -> {
                localX = new Vec3(sign, 0, 0);   // NORTH -> -X, SOUTH -> +X
                localY = new Vec3(0, 1, 0);
            }
            case Y -> {
                localX = new Vec3(1, 0, 0);      // always +X
                localY = new Vec3(0, 0, sign);   // UP -> +Z, DOWN -> -Z
            }
        }

        Vec3 center = blockPos.above(3).getCenter();
        long gameTime = level.getGameTime();

        // Calculate current wave progress (0 to 1)
        int cycleTicks = StargateConfig.client().puddleCycleTicks();
        float progress = (gameTime % cycleTicks) / (float) cycleTicks;
        float currentRadius = progress * MAX_RADIUS;

        // Spawn a dense ring at the current radius
        int ringParticleCount = Math.max((int) (currentRadius * 20 + 12), 20); // More particles for larger rings
        int bgColor = currentRadius <= INNER_WHITE_RADIUS ? INNER_BG_COLOR : OUTER_BG_COLOR;

        // Only spawn a few particles at the center as a source
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

                // Give center particles slight random outward velocity
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

        // Main expanding ring - dense enough to look continuous
        for (int i = 0; i < ringParticleCount; i++) {
            float angle = (2 * Mth.PI * i) / ringParticleCount;
            float shiftedAngle = angle + (gameTime * 0.2f); // Rotation for ripple effect

            // Add slight random variation to make it look organic
            float jitter = 0.05f;
            float radiusJitter = currentRadius + (float)(Math.random() - 0.5) * jitter;
            float angleJitter = (float)(Math.random() - 0.5) * 0.1f;
            float finalAngle = shiftedAngle + angleJitter;

            float offsetX = Mth.cos(finalAngle) * radiusJitter;
            float offsetY = Mth.sin(finalAngle) * radiusJitter;
            Vector2f uv = toEventHorizonUv(offsetX, offsetY);

            // Outward velocity - faster at larger radii for ripple effect
            float speed = 0.3f + (currentRadius / MAX_RADIUS) * 0.3f;
            double velX = Mth.cos(finalAngle) * speed;
            double velY = Mth.sin(finalAngle) * speed;

            // Transform velocity to world space
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

        // Ripple effect - secondary wave trailing behind with its own color
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

    private static Vector2f toEventHorizonUv(float offsetX, float offsetY) {
        float u = (float) (offsetX / (MAX_RADIUS * 2.0) + 0.5);
        float v = (float) (offsetY / (MAX_RADIUS * 2.0) + 0.5);
        return new Vector2f(Mth.clamp(u, 0, 1), Mth.clamp(1 - v, 0, 1));
    }

    @Override
    public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) { }
}
