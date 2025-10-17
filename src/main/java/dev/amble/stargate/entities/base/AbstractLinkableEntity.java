package dev.amble.stargate.entities.base;

import dev.amble.stargate.api.data.StargateRef;
import dev.amble.stargate.api.data.StargateLinkable;
import dev.amble.stargate.api.Stargate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface AbstractLinkableEntity extends StargateLinkable {

    StargateRef gateRef();

    World getWorld();

    DataTracker getDataTracker();

    TrackedData<Optional<Long>> getTracked();

    @Override
    default @Nullable Stargate asGate() {
        return gateRef().asGate();
    }

    @Override
    default boolean link(@Nullable Stargate gate) {
        if (!gateRef().link(gate)) return false;

        this.getDataTracker().set(this.getTracked(), Optional.of(gateRef().address()));
        return true;
    }

    @Override
    default boolean link(long address) {
        if (!gateRef().link(address)) return false;

        this.getDataTracker().set(this.getTracked(), Optional.of(gateRef().address()));
        return true;
    }

    @Override
    default void unlink() {
        gateRef().unlink();
        this.getDataTracker().set(this.getTracked(), Optional.empty());
    }

    default void initDataTracker() {
        this.getDataTracker().startTracking(this.getTracked(), Optional.empty());
    }

    default void onTrackedDataSet(TrackedData<?> data) {
        if (!this.getTracked().equals(data))
            return;

        this.getDataTracker().get(this.getTracked()).ifPresent(this::link);
    }

    default void readCustomDataFromNbt(NbtCompound nbt) {
        gateRef().readNbt(nbt);
    }

    default void writeCustomDataToNbt(NbtCompound nbt) {
        gateRef().writeNbt(nbt);
    }

    TrackedDataHandler<Optional<Long>> OPTIONAL_LONG = TrackedDataHandler.ofOptional(PacketByteBuf::writeVarLong, PacketByteBuf::readVarLong);

    static <T extends Entity & AbstractLinkableEntity> TrackedData<Optional<Long>> register(Class<T> self) {
        return DataTracker.registerData(self, OPTIONAL_LONG);
    }
}