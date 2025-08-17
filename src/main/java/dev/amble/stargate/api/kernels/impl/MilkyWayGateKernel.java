package dev.amble.stargate.api.kernels.impl;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.kernels.BasicStargateKernel;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.util.Identifier;

public class MilkyWayGateKernel extends BasicStargateKernel {

    public static final Identifier ID = StargateMod.id("milky_way");

    public MilkyWayGateKernel(Stargate parent) {
        super(ID, parent);
    }

    @Override
    public long energyToDial(Address address) {
        return (long) (this.address.distanceTo(address).distance() * 100);
    }

    @Override
    public boolean canDialTo(Stargate stargate) {
        return super.canDialTo(stargate) && stargate.kernel() instanceof MilkyWayGateKernel;
    }
}
