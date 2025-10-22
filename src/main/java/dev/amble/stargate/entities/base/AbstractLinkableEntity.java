package dev.amble.stargate.entities.base;

import dev.amble.stargate.api.data.StargateRef;
import dev.amble.stargate.api.data.StargateLinkable;
import dev.amble.stargate.api.Stargate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface AbstractLinkableEntity extends StargateLinkable {

    StargateRef gateRef();

    World getWorld();

    DataTracker getDataTracker();

    TrackedData<Long> getTracked();

    @Override
    default @Nullable Stargate asGate() {
        return gateRef().asGate();
    }

    @Override
    default boolean link(@Nullable Stargate gate) {
        if (!gateRef().link(gate)) return false;

        this.getDataTracker().set(this.getTracked(), gateRef().address());
        return true;
    }

    @Override
    default boolean link(long address) {
        if (!gateRef().link(address)) return false;

        this.getDataTracker().set(this.getTracked(), gateRef().address());
        return true;
    }

    @Override
    default void unlink() {
        gateRef().unlink();
        this.getDataTracker().set(this.getTracked(), -1L);
    }

    default void initDataTracker() {
        this.getDataTracker().startTracking(this.getTracked(), -1L);
    }

    default void onTrackedDataSet(TrackedData<?> data) {
        if (!this.getTracked().equals(data))
            return;

        this.link(this.getDataTracker().get(this.getTracked()));
    }

    default void readCustomDataFromNbt(NbtCompound nbt) {
        gateRef().readNbt(nbt);
    }

    default void writeCustomDataToNbt(NbtCompound nbt) {
        gateRef().writeNbt(nbt);
    }

    static <T extends Entity & AbstractLinkableEntity> TrackedData<Long> register(Class<T> self) {
        return DataTracker.registerData(self, TrackedDataHandlerRegistry.LONG);
    }
}