package dev.amble.stargate.api.v2;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import net.minecraft.util.Identifier;

public class OrlinGateKernel extends StargateKernel.Basic {

    public static final Identifier ID = StargateMod.id("orlin");

    public OrlinGateKernel(Stargate parent) {
        super(ID, parent);
    }

    @Override
    public long energyToDial(Address address) {
        return (long) (this.address.distanceTo(address).distance() * 100);
    }

    @Override
    public boolean canDialTo(Stargate stargate) {
        return stargate.kernel instanceof OrlinGateKernel;
    }

    @Override
    public GateShape shape() {
        // Empty shape because the Orlin gate is so small the collisions come from a custom VoxelShape in the block - Loqor
        return GateShape.generated("");
    }
}
