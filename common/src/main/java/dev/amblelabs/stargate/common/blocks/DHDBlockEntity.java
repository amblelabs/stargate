package dev.amblelabs.stargate.common.blocks;

import dev.amblelabs.stargate.api.ecs.event.DHDBlockEvents;
import dev.amblelabs.stargate.api.util.BlockEntityHelper;
import dev.amblelabs.stargate.common.entities.DHDControlEntity;
import dev.amblelabs.stargate.common.lib.StargateBlockEntities;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DHDBlockEntity extends BlockEntity implements GeoBlockEntity, BlockEntityHelper.Ticking {

    private static final String TAG_CONTROLS = "Controls";
    private static final String TAG_INITIALIZED = "Initialized";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean controlsInitialized = false;
    private final List<UUID> controlEntityUuids = new ArrayList<>();

    private static final int RING_COUNT = 18;
    private static final double INNER_RING_RADIUS = 0.4;
    private static final double OUTER_RING_RADIUS = 0.8;

    public DHDBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(StargateBlockEntities.DHD.get(), blockPos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        TEvents.handle(new DHDBlockEvents.RegisterControllers(this, controllers));
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
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide()) return;

        if (!controlsInitialized) {
            spawnRings((ServerLevel) level, blockPos, blockState);
            controlsInitialized = true;
            setChanged();
        }
    }

    private void spawnRings(ServerLevel level, BlockPos pos, BlockState state) {
        controlEntityUuids.clear();

        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        float baseYaw = facing.toYRot();

        double cx = pos.getX() + 0.5;
        double cy = pos.getY() + 0.8;
        double cz = pos.getZ() + 0.5;

        int currentIndex = 0;

        spawnButton(level, cx, cy, cz, 0, 0, currentIndex++, baseYaw, true);

        for (int i = 0; i < RING_COUNT; i++) {
            double angle = (2 * Math.PI * i) / RING_COUNT;
            double offsetX = Math.cos(angle) * INNER_RING_RADIUS;
            double offsetZ = Math.sin(angle) * INNER_RING_RADIUS;

            spawnButton(level, cx, cy, cz, offsetX, offsetZ, currentIndex++, baseYaw, false);
        }

        double staggerOffset = Math.PI / RING_COUNT;
        for (int i = 0; i < RING_COUNT; i++) {
            double angle = ((2 * Math.PI * i) / RING_COUNT) + staggerOffset;
            double offsetX = Math.cos(angle) * OUTER_RING_RADIUS;
            double offsetZ = Math.sin(angle) * OUTER_RING_RADIUS;

            spawnButton(level, cx, cy, cz, offsetX, offsetZ, currentIndex++, baseYaw, false);
        }
    }

    private void spawnButton(ServerLevel level, double cx, double cy, double cz, double offsetX, double offsetZ, int index, float yaw, boolean isCenter) {
        DHDControlEntity entity = new DHDControlEntity(level);

        entity.setPos(cx + offsetX, cy, cz + offsetZ);
        entity.setYRot(yaw);

        entity.setButtonIndex(index);
        entity.setDhdBlockPos(this.worldPosition);

        level.addFreshEntity(entity);
        controlEntityUuids.add(entity.getUUID());
    }

    public void onBlockBreak(ServerLevel level) {
        for (UUID uuid : controlEntityUuids) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) {
                entity.discard();
            }
        }
        controlEntityUuids.clear();
        controlsInitialized = false;
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean(TAG_INITIALIZED, this.controlsInitialized);

        ListTag listTag = new ListTag();
        for (UUID uuid : this.controlEntityUuids) {
            listTag.add(NbtUtils.createUUID(uuid));
        }

        tag.put(TAG_CONTROLS, listTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.controlsInitialized = tag.getBoolean(TAG_INITIALIZED);
        this.controlEntityUuids.clear();

        if (tag.contains(TAG_CONTROLS, Tag.TAG_LIST)) {
            ListTag listTag = tag.getList(TAG_CONTROLS, Tag.TAG_COMPOUND);

            for (Tag value : listTag) {
                this.controlEntityUuids.add(NbtUtils.loadUUID(value));
            }
        }
    }
}
