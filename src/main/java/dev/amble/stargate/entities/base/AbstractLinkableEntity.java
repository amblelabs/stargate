package dev.amble.stargate.entities.base;

import dev.amble.stargate.api.network.StargateLinkable;
import dev.amble.stargate.api.network.StargateRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public interface AbstractLinkableEntity extends StargateLinkable {

    World getWorld();

    DataTracker getDataTracker();

    TrackedData<Optional<UUID>> getTracked();

    StargateRef asRef();

    void setRef(StargateRef ref);

    @Override
    default void setStargate(StargateRef stargate) {
        this.setRef(stargate);
        this.getDataTracker().set(this.getTracked(), Optional.of(stargate.id()));
    }

    @Override
    default StargateRef gate() {
        StargateRef result = this.asRef();

        if (result == null) {
            UUID id = this.getDataTracker().get(this.getTracked()).orElse(null);
            this.link(new StargateRef(id, ((Entity) this).getWorld().isClient()));
            return this.gate();
        }

        return result;
    }

    default void initDataTracker() {
        this.getDataTracker().startTracking(this.getTracked(), Optional.empty());
    }

    default void onTrackedDataSet(TrackedData<?> data) {
        if (!this.getTracked().equals(data))
            return;

        UUID id = this.getDataTracker().get(this.getTracked()).orElse(null);
        this.link(new StargateRef(id, ((Entity) this).getWorld().isClient()));
    }

    default void readCustomDataFromNbt(NbtCompound nbt) {
        NbtElement rawId = nbt.get("Stargate");

        if (rawId == null)
            return;

        UUID id = NbtHelper.toUuid(rawId);
        this.link(new StargateRef(id, ((Entity) this).getWorld().isClient()));

        if (this.getWorld() == null)
            return;

        this.onLinked();
    }

    default void writeCustomDataToNbt(NbtCompound nbt) {
        StargateRef ref = this.asRef();

        if (ref != null && ref.id() != null)
            nbt.putUuid("Stargate", ref.id());
    }

    static <T extends Entity & AbstractLinkableEntity> TrackedData<Optional<UUID>> register(Class<T> self) {
        return DataTracker.registerData(self, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }
}