package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;

public class StargateBlockEntity extends BlockEntity implements GeoBlockEntity, BlockEntityTicker {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public final Stargate stargate = new Stargate();

    public StargateBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public StargateBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(StargateBlockEntities.STARGATE, blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        boolean isClient = this.level != null && this.level.isClientSide();
        stargate.toNbt(nbt, isClient);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        boolean isClient = this.level != null && this.level.isClientSide();
        stargate.fromNbt(nbt, isClient);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new StargateBlockEvents.RegisterControllers(this, controllers));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null || level.isClientSide()) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        BlockPos centerPos = blockPos.above().above().above();
        double maxRadius = 2.55d;
        double innerWhiteRadius = 0.8d;

        double centerX = centerPos.getX() + 0.5;
        double centerY = centerPos.getY() + 0.5;
        double centerZ = centerPos.getZ() + 0.5;

        long gameTime = level.getGameTime();

        // --- EFFECT 1: LIQUID BACKGROUND (Less Noisy, Fluid Undulation) ---
        // Incrementing by 0.25 reduces density overlapping and performance overhead
        for (double radius = 0.1; radius <= maxRadius; radius += 0.25) {

            // Add a subtle sine-wave pulsation to background ring density over time
            double waveOffset = Math.sin((gameTime * 0.1) + radius) * 2;
            int bgCount = (int) (radius * 12 + waveOffset);
            if (bgCount < 1) bgCount = 1;

            int bgColor = (radius <= innerWhiteRadius)
                    ? Color.ofRGB(0.85f, 0.85f, 1.0f).getColor() // Soft off-white
                    : Color.ofRGB(0.05f, 0.75f, 1.0f).getColor(); // Deep, shimmering gate blue

            for (int i = 0; i < bgCount; i++) {
                double angle = (2 * Math.PI * i) / bgCount;

                // Introduce time-based rotation so the background slowly shifts like liquid
                double shiftedAngle = angle + (gameTime * 0.015);

                double offsetX = Math.cos(shiftedAngle) * radius;
                double offsetY = Math.sin(shiftedAngle) * radius;

                // Spreading particles slightly via the speed parameters breaks up harsh concentric noise lines
                serverLevel.sendParticles(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, new Vector2f((float) offsetX, (float) offsetY)),
                        centerX + offsetX, centerY + offsetY, centerZ,
                        1,
                        0.04, 0.04, 0.00, // Small random positional jitter (X, Y, Z) to blend gaps smoothly
                        0.0
                );
            }
        }

        // --- EFFECT 2: CLEAN RIPPLE (Slower, Smooth Event Horizon Wave) ---
        int cycleLength = 35; // Increased cycle length makes the ripple travel slower and look heavier/more fluid
        double progress = (gameTime % cycleLength) / (double) cycleLength;

        double rippleRadius = progress * maxRadius;
        if (rippleRadius < 0.1) rippleRadius = 0.1;

        int rippleCount = (int) (rippleRadius * 16); // Lowered particle count prevents clustering noise

        int rippleColor = (rippleRadius <= innerWhiteRadius)
                ? Color.ofRGB(1.0f, 1.0f, 1.0f).getColor()
                : Color.ofRGB(0.2f, 0.65f, 1.0f).getColor(); // Vibrant, luminous blue highlight

        for (int i = 0; i < rippleCount; i++) {
            double angle = (2 * Math.PI * i) / rippleCount;
            double offsetX = Math.cos(angle) * rippleRadius;
            double offsetY = Math.sin(angle) * rippleRadius;

            serverLevel.sendParticles(
                    new PuddleParticleOptions(StargateParticles.PUDDLE, rippleColor, new Vector2f((float) offsetX, (float) offsetY)),
                    centerX + offsetX, centerY + offsetY, centerZ,
                    1,
                    0.02, 0.02, 0.00, // Tiny jitter keeps the expanding wave unified but organic
                    0.0
            );
        }
    }
}
