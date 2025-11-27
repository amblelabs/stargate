package dev.amble.stargate.fluid;

import dev.amble.lib.container.impl.FluidContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StargateFluids implements FluidContainer {

    public static final FlowableFluid LIQUID_NAQUADAH_STILL = new LiquidNaquadahFluid.Still();
    public static final FlowableFluid LIQUID_NAQUADAH_FLOWING = new LiquidNaquadahFluid.Flowing();

    private static final String STILL_SUFFIX = "_still";
    private static final String FLOWING_SUFFIX = "_flowing";

    private final Map<Identifier, FlowableFluid> fluids = new HashMap<>();

    @Override
    public void postProcessField(Identifier identifier, Fluid value, Field field) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            this.postProcessClient(identifier, value);
    }

    private void postProcessClient(Identifier identifier, Fluid value) {
        if (!(value instanceof FlowableFluid flowable)) return;

        boolean still = identifier.getPath().endsWith(STILL_SUFFIX);

        Identifier realId = identifier.withPath(s -> s.substring(0, s.length() - (still ? STILL_SUFFIX.length() : FLOWING_SUFFIX.length())));
        FlowableFluid prev = fluids.put(realId, flowable);

        if (prev != null) {
            FlowableFluid stillFluid = still ? flowable : prev;
            FlowableFluid flowingFluid = still ? prev : flowable;

            FluidRenderHandlerRegistry.INSTANCE.register(stillFluid, flowingFluid,
                    new SimpleFluidRenderHandler(
                            realId.withPath(s -> "block/" + s + STILL_SUFFIX),
                            realId.withPath(s -> "block/" + s + FLOWING_SUFFIX)
                    ));
        }
    }

    @Override
    public void finish() {
        fluids.clear();
    }
}