package dev.amble.stargate.fluid;

import dev.amble.lib.container.impl.FluidContainer;
import net.minecraft.fluid.FlowableFluid;

public class StargateFluids implements FluidContainer {

    public static final FlowableFluid STILL_LIQUID_NAQUADAH = new LiquidNaquadahFluid.Still();
    public static final FlowableFluid FLOWING_LIQUID_NAQUADAH = new LiquidNaquadahFluid.Flowing();
}