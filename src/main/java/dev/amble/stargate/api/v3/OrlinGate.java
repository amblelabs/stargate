package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.kernels.GateShape;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientOrlinState;
import dev.drtheo.yaar.state.TState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class OrlinGate extends Stargate {

    public static Identifier ID = StargateMod.id("orlin");

    public OrlinGate(DirectedGlobalPos pos) {
        super(pos);
    }

    public OrlinGate(NbtCompound nbt, boolean isClient) {
        super(nbt, isClient);
    }

    @Override
    protected void attachClientState() {
        this.addState(new ClientOrlinState());
    }

    @Override
    public GateShape shape() {
        return GateShape.EMPTY; // collision handled by the block itself
    }

    @Override
    protected TState<?> createDefaultState() {
        return new BasicGateStates.Closed();
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
