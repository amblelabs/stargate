package dev.amble.stargate.api.v3;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.kernels.GateShape;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.drtheo.yaar.state.TState;
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
    public GateShape shape() {
        return GateShape.DEFAULT;
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
