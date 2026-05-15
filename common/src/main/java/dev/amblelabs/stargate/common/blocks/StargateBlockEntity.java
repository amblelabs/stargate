package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.common.particles.PuddleParticleOptions;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
        stargate.toNbt(nbt, NbtSerializer.Context.fromLevel(level));
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        stargate.fromNbt(nbt, NbtDeserializer.Context.fromLevel(level));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new StargateBlockEvents.RegisterControllers(this.stargate, this, controllers));
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
        if (!level.isClientSide()) return;

        BlockPos centerPos = blockPos.above().above().above();
        double maxRadius = 2.6d;
        double innerWhiteRadius = 0.8d;

        double centerX = centerPos.getX() + 0.5;
        double centerY = centerPos.getY() + 0.5;
        double centerZ = centerPos.getZ() + 0.5;

        long gameTime = level.getGameTime();

        for (double radius = 0.1; radius <= maxRadius; radius += 0.25) {

            double waveOffset = Math.sin((gameTime * 0.1) + radius) * 2;
            int bgCount = (int) (radius + waveOffset);
            if (bgCount < 1) bgCount = 1;

            int bgColor = (radius <= innerWhiteRadius)
                    ? Color.ofRGB(0.85f, 0.85f, 1.0f).getColor()
                    : Color.ofRGB(0.05f, 0.75f, 1.0f).getColor();

            for (int i = 0; i < bgCount; i++) {
                double angle = (2 * Math.PI * i) / bgCount;

                double shiftedAngle = angle + (gameTime * 0.25);

                double offsetX = Math.cos(shiftedAngle) * radius;
                double offsetY = Math.sin(shiftedAngle) * radius;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY, maxRadius);

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, uv),
                        centerX + offsetX, centerY + offsetY, centerZ,
                        1,
                        0, 0
                );
            }
        }

        int cycleLength = 35;
        double progress = (gameTime % cycleLength) / (double) cycleLength;

        double rippleRadius = progress * maxRadius;
        if (rippleRadius < 0.1) rippleRadius = 0.1;

        int rippleCount = (int) (rippleRadius);

        int rippleColor = (rippleRadius <= innerWhiteRadius)
                ? Color.ofRGB(1.0f, 1.0f, 1.0f).getColor()
                : Color.ofRGB(0.2f, 0.65f, 1.0f).getColor();

        for (int i = 0; i < rippleCount; i++) {
            double angle = (2 * Math.PI * i) / rippleCount;
            double offsetX = Math.cos(angle) * rippleRadius;
            double offsetY = Math.sin(angle) * rippleRadius;
            Vector2f uv = toEventHorizonUv(offsetX, offsetY, maxRadius);
            level.addAlwaysVisibleParticle(
                    new PuddleParticleOptions(StargateParticles.PUDDLE, rippleColor, uv),
                    centerX + offsetX, centerY + offsetY, centerZ,
                    1,
                    0, 0
            );
        }
    }

    private static Vector2f toEventHorizonUv(double offsetX, double offsetY, double maxRadius) {
        float u = (float) ((offsetX / (maxRadius * 2.0)) + 0.5);
        float v = (float) ((offsetY / (maxRadius * 2.0)) + 0.5);
        return new Vector2f(Mth.clamp(u, 0.0f, 1.0f), Mth.clamp(1.0f - v, 0.0f, 1.0f));
    }
}
