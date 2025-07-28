package dev.amble.stargate.api.kernels;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.NbtSync;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public interface StargateKernel extends NbtSync {

    default void onCreate(DirectedGlobalPos pos) {}

    default void tick() { }

    boolean canTeleportFrom(LivingEntity entity);
    void tryTeleportFrom(LivingEntity entity);

    Address address();

    long energy();
    long maxEnergy();

    long energyToDial(Address address);

    default boolean hasEnoughEnergy(Address address) {
        return this.maxEnergy() == -1 || this.energy() >= this.energyToDial(address);
    }

    boolean canDialTo(Stargate stargate);

    GateShape shape();
    GateState state();

    boolean dirty();
    void markDirty();
    void unmarkDirty();

    interface Mutable extends StargateKernel {
        void setState(GateState state);
    }

    interface Impl extends Mutable {
        Identifier id();
    }
}
