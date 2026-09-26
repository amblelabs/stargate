package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.items.component.StargateLinkedComponent;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class StargateComponents {

    private static final XplatRegistrar<DataComponentType<?>> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.DATA_COMPONENT_TYPE);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Supplier<DataComponentType<StargateLinkedComponent>> STARGATE = make("stargate",
            builder -> builder.persistent(StargateLinkedComponent.CODEC)
                    .networkSynchronized(StargateLinkedComponent.STREAM_CODEC).cacheEncoding());

    @SuppressWarnings("SameParameterValue")
    private static <T> Supplier<DataComponentType<T>> make(String name, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return make(StargateAPI.modLoc(name), unaryOperator);
    }

    private static <T> Supplier<DataComponentType<T>> make(ResourceLocation loc, UnaryOperator<DataComponentType.Builder<T>> op) {
        return REGISTRAR.register(loc, () -> op.apply(DataComponentType.builder()).build());
    }
}
