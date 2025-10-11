package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.state.stargate.client.ClientMilkyWayState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class MilkyWayGate extends Stargate {

    public static Identifier ID = StargateMod.id("milky_way");

    public MilkyWayGate(DirectedGlobalPos pos) {
        super(pos);
    }

    public MilkyWayGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientMilkyWayState());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
