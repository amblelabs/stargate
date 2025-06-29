package dev.amble.stargate.api.v2;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import net.minecraft.util.Identifier;

public class UniverseGateKernel extends StargateKernel.Basic {

    public static final Identifier ID = StargateMod.id("universe");

    public UniverseGateKernel(Stargate parent) {
        super(ID, parent);
    }

    @Override
    public long maxEnergy() {
        return Long.MAX_VALUE;
    }

    @Override
    public long energyToDial(Address address) {
        return (long) (this.address.distanceTo(address).distance() * 100);
    }

    @Override
    public GateShape shape() {
        return GateShape.DEFAULT;
    }
}
