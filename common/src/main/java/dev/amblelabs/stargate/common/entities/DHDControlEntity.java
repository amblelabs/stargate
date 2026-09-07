package dev.amblelabs.stargate.common.entities;

import dev.amblelabs.stargate.common.blocks.DHDBlockEntity;
import dev.amblelabs.stargate.common.lib.StargateEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DHDControlEntity extends Entity {

    public static final String DHD_BLOCK_POS = "dhd_block_pos";
    private static final EntityDataAccessor<Integer> BUTTON_INDEX = SynchedEntityData.defineId(DHDControlEntity.class, EntityDataSerializers.INT);

    public BlockPos dhdBlockPos = new BlockPos(0, 0, 0);

    public DHDControlEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public DHDControlEntity(Level level) {
        super(StargateEntities.DHD_CONTROL.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BUTTON_INDEX, -1);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return run() ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public boolean run() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return false;

        if (!(serverLevel.getBlockEntity(this.getDhdBlockPos()) instanceof DHDBlockEntity dhdBlockEntity)) return false;

        System.out.println("Pressed button index: " + getButtonIndex());

        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        EntityDimensions baseDimensions = super.getDimensions(pose);
        if (getButtonIndex() == 0) {
            return baseDimensions.scale(1.5f);
        }
        return baseDimensions;
    }

    public void setButtonIndex(int index) {
        this.entityData.set(BUTTON_INDEX, index);
        this.refreshDimensions();
    }

    public int getButtonIndex() {
        return this.entityData.get(BUTTON_INDEX);
    }

    public void setDhdBlockPos(BlockPos pos) {
        this.dhdBlockPos = pos;
    }

    public BlockPos getDhdBlockPos() {
        return this.dhdBlockPos;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(DHD_BLOCK_POS)) {
            NbtUtils.readBlockPos(compound, DHD_BLOCK_POS).ifPresent(this::setDhdBlockPos);
        }
        if (compound.contains("button_index")) {
            setButtonIndex(compound.getInt("button_index"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put(DHD_BLOCK_POS, NbtUtils.writeBlockPos(this.getDhdBlockPos()));
        compound.putInt("button_index", getButtonIndex());
    }
}