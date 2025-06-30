package dev.amble.stargate.api.v2.kernels;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.v2.GateShape;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.v2.StargateKernel;
import net.minecraft.util.Identifier;

public class DestinyGateKernel extends StargateKernel.Basic {

    public static final Identifier ID = StargateMod.id("destiny");

    public DestinyGateKernel(Stargate parent) {
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
