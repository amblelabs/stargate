package dev.amble.stargate.entities.base;

import dev.amble.stargate.api.data.StargateRef;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

@Deprecated(since = "0.0.0", forRemoval = true)
public abstract class LinkableDummyLivingEntity extends DummyLivingEntity implements AbstractLinkableEntity {

    private static final TrackedData<Long> STARGATE = AbstractLinkableEntity
            .register(LinkableDummyLivingEntity.class);

    private final StargateRef stargate = new StargateRef(this::getWorld);

    public LinkableDummyLivingEntity(EntityType<? extends LivingEntity> type, World world, boolean hasBrain) {
        super(type, world, hasBrain);
    }

    @Override
    public StargateRef gateRef() {
        return stargate;
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public DataTracker getDataTracker() {
        return super.getDataTracker();
    }

    @Override
    public TrackedData<Long> getTracked() {
        return STARGATE;
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        AbstractLinkableEntity.super.initDataTracker();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        AbstractLinkableEntity.super.onTrackedDataSet(data);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        AbstractLinkableEntity.super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        AbstractLinkableEntity.super.writeCustomDataToNbt(nbt);
    }
}
