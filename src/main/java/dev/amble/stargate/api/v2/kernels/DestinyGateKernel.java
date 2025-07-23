package dev.amble.stargate.api.v2.kernels;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.v2.kernels.base.BasicStargateKernel;
import net.minecraft.util.Identifier;

public class DestinyGateKernel extends BasicStargateKernel {

    public static final Identifier ID = StargateMod.id("destiny");

    public DestinyGateKernel(Stargate parent) {
        super(ID, parent);
    }

    @Override
    public long maxEnergy() {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean canDialTo(Stargate stargate) {
        return super.canDialTo(stargate) && stargate.kernel() instanceof DestinyGateKernel
                && this.address.distanceTo(address).distance() <= 1500d;
    }

    @Override
    public long energyToDial(Address address) {
        return (long) (this.address.distanceTo(address).distance() * 100);
    }
}
