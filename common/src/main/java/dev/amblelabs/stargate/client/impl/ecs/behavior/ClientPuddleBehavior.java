package dev.amblelabs.stargate.client.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
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
    private static final int CYCLE_LENGTH = 35;

    private static final int INNER_BG_COLOR = Color.ofRGB(0.85f, 0.85f, 1.0f).getColor();
    private static final int OUTER_BG_COLOR = Color.ofRGB(0.05f, 0.75f, 1.0f).getColor();

    private static final int INNER_RIPPLE_COLOR = Color.WHITE.getColor();
    private static final int OUTER_RIPPLE_COLOR = Color.ofRGB(0.2f, 0.65f, 1.0f).getColor();

    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide() || mc.player == null || mc.player.tickCount % StargateConfig.client().puddleParticleTick() != 0) return;

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

        for (float radius = 0.1f; radius <= MAX_RADIUS; radius += 0.25f) {
            double waveOffset = Mth.sin((gameTime * 0.1f) + radius) * 2;

            int bgCount = Math.max((int) (radius + waveOffset), 1);
            int bgColor = radius <= INNER_WHITE_RADIUS ? INNER_BG_COLOR : OUTER_BG_COLOR;

            for (int i = 0; i < bgCount; i++) {
                float angle = (2 * Mth.PI * i) / bgCount;
                float shiftedAngle = angle + (gameTime * 0.25f);

                float offsetX = Mth.cos(shiftedAngle) * radius;
                float offsetY = Mth.sin(shiftedAngle) * radius;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                double worldX = center.x + (offsetX * localX.x) + (offsetY * localY.x);
                double worldY = center.y + (offsetX * localX.y) + (offsetY * localY.y);
                double worldZ = center.z + (offsetX * localX.z) + (offsetY * localY.z);

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, uv),
                        worldX, worldY, worldZ,
                        1, 0, 0
                );
            }
        }

        float progress = (gameTime % CYCLE_LENGTH) / (float) CYCLE_LENGTH;
        float rippleRadius = Math.min(progress * MAX_RADIUS, 0.1f);
        int rippleCount = (int) rippleRadius;

        int rippleColor = rippleRadius <= INNER_WHITE_RADIUS ? INNER_RIPPLE_COLOR : OUTER_RIPPLE_COLOR;

        for (int i = 0; i < rippleCount; i++) {
            float angle = (2 * Mth.PI * i) / rippleCount;
            float offsetX = Mth.cos(angle) * rippleRadius;
            float offsetY = Mth.sin(angle) * rippleRadius;

            Vector2f uv = toEventHorizonUv(offsetX, offsetY);

            double worldX = center.x + (offsetX * localX.x) + (offsetY * localY.x);
            double worldY = center.y + (offsetX * localX.y) + (offsetY * localY.y);
            double worldZ = center.z + (offsetX * localX.z) + (offsetY * localY.z);

            level.addAlwaysVisibleParticle(
                    new PuddleParticleOptions(StargateParticles.PUDDLE, rippleColor, uv),
                    worldX, worldY, worldZ,
                    1, 0, 0
            );
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
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Player player, BlockHitResult blockHitResult) { }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) { }
}
