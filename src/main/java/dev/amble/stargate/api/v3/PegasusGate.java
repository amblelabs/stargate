package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v3.state.stargate.client.ClientPegasusState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class PegasusGate extends Stargate {

    public static Identifier ID = StargateMod.id("pegasus");

    public PegasusGate(DirectedGlobalPos pos) {
        super(pos);
    }

    public PegasusGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientPegasusState());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
