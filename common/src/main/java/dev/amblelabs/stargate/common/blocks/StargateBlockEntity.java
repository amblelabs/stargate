package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.NbtSerializer;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateLifecycleEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.stargate.StargateNetwork;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class StargateBlockEntity extends BlockEntity implements GeoBlockEntity, Stargate.UpdateListener,
        BlockEntityHelper.Placeable, BlockEntityHelper.Ticking {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public @Nullable Stargate stargate = null;

    public StargateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(StargateBlockEntities.STARGATE, blockPos, blockState);
    }

    public Stargate setStargate(Stargate stargate, NbtDeserializer.Context ctx) {
        stargate.onUpdate(this);
        TEvents.handle(new StargateLifecycleEvents.Instantiate(stargate, ctx));
        return this.stargate = stargate;
    }

    @Override
    public void onPlace(BlockState blockState, ServerLevel level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        Stargate stargate = StargateNetwork.getOrCreate(level).create();

        PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().getAny().orElseThrow().value();
        entry.mark(stargate);

        this.setStargate(stargate, NbtDeserializer.Context.fromLevel(level));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return super.saveWithoutMetadata(provider);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        stargate.toNbt(nbt, NbtSerializer.Context.fromLevel(level));
        nbt.putUUID("Ref", this.stargate.getId()); // TODO: figure out if this is needed
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        NbtDeserializer.Context ctx = NbtDeserializer.Context.fromLevel(level);

        this.stargate = this.stargate != null ? this.stargate.fromNbt(nbt, ctx)
                : this.setStargate(Stargate.createFromNbt(nbt, ctx), ctx);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new StargateBlockEvents.RegisterControllers(Objects.requireNonNull(this.stargate), this, controllers));
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
    public void onStargateUpdate(Stargate stargate) {
        this.setChanged();
    }

    private static final float MAX_RADIUS = 2.6f;
    private static final float INNER_WHITE_RADIUS = 0.8f;
    private static final int CYCLE_LENGTH = 35;

    private static final int INNER_BG_COLOR = Color.ofRGB(0.85f, 0.85f, 1.0f).getColor();
    private static final int OUTER_BG_COLOR = Color.ofRGB(0.05f, 0.75f, 1.0f).getColor();

    private static final int INNER_RIPPLE_COLOR = Color.WHITE.getColor();
    private static final int OUTER_RIPPLE_COLOR = Color.ofRGB(0.2f, 0.65f, 1.0f).getColor();

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide()) return;

        BlockPos centerPos = blockPos.above().above().above();

        double centerX = centerPos.getX() + 0.5;
        double centerY = centerPos.getY() + 0.5;
        double centerZ = centerPos.getZ() + 0.5;

        long gameTime = level.getGameTime();

        for (float radius = 0.1f; radius <= MAX_RADIUS; radius += 0.25f) {
            double waveOffset = Mth.sin((gameTime * 0.1f) + radius) * 2;

            int bgCount = Math.min((int) (radius + waveOffset), 1);
            int bgColor = radius <= INNER_WHITE_RADIUS ? INNER_BG_COLOR : OUTER_BG_COLOR;

            for (int i = 0; i < bgCount; i++) {
                float angle = (2 * Mth.PI * i) / bgCount;
                float shiftedAngle = angle + (gameTime * 0.25f);

                float offsetX = Mth.cos(shiftedAngle) * radius;
                float offsetY = Mth.sin(shiftedAngle) * radius;
                Vector2f uv = toEventHorizonUv(offsetX, offsetY);

                level.addAlwaysVisibleParticle(
                        new PuddleParticleOptions(StargateParticles.PUDDLE, bgColor, uv),
                        centerX + offsetX, centerY + offsetY, centerZ,
                        1, 0, 0
                );
            }
        }

        float progress = (gameTime % CYCLE_LENGTH) / (float) CYCLE_LENGTH;

        float rippleRadius = Math.min(progress * MAX_RADIUS, 0.1f);
        int rippleCount = (int) rippleRadius;

        int rippleColor = rippleRadius <= INNER_WHITE_RADIUS
                ? INNER_RIPPLE_COLOR
                : OUTER_RIPPLE_COLOR;

        for (int i = 0; i < rippleCount; i++) {
            float angle = (2 * Mth.PI * i) / rippleCount;
            float offsetX = Mth.cos(angle) * rippleRadius;
            float offsetY = Mth.sin(angle) * rippleRadius;

            Vector2f uv = toEventHorizonUv(offsetX, offsetY);
            level.addAlwaysVisibleParticle(
                    new PuddleParticleOptions(StargateParticles.PUDDLE, rippleColor, uv),
                    centerX + offsetX, centerY + offsetY, centerZ,
                    1, 0, 0
            );
        }
    }

    private static Vector2f toEventHorizonUv(float offsetX, float offsetY) {
        float u = (float) (offsetX / (MAX_RADIUS * 2.0) + 0.5);
        float v = (float) (offsetY / (MAX_RADIUS * 2.0) + 0.5);
        return new Vector2f(Mth.clamp(u, 0, 1), Mth.clamp(1 - v, 0, 1));
    }
}
