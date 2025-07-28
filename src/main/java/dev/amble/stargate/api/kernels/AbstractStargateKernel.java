package dev.amble.stargate.api.kernels;

import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.util.Identifier;

public abstract class AbstractStargateKernel implements StargateKernel {

    private final Identifier id;
    protected final Stargate parent;

    protected GateState state;
    protected boolean dirty;

    public AbstractStargateKernel(Identifier id, Stargate parent) {
        this.id = id;
        this.parent = parent;
    }

    public Identifier id() {
        return id;
    }

    @Override
    public GateState state() {
        return state;
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void unmarkDirty() {
        this.dirty = false;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }
}
