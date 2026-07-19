package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.items.component.StargateLinkedComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public class StargateComponents {

    public static void registerComponents(BiConsumer<DataComponentType<?>, ResourceLocation> r) {
        for (var e : COMPONENTS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, DataComponentType<?>> COMPONENTS = new LinkedHashMap<>();

    public static final DataComponentType<StargateLinkedComponent> STARGATE = make("stargate",
            builder -> builder.persistent(StargateLinkedComponent.CODEC)
                    .networkSynchronized(StargateLinkedComponent.STREAM_CODEC).cacheEncoding());

    private static <T> DataComponentType<T> make(String name, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        return make(StargateAPI.modLoc(name), unaryOperator);
    }

    private static <T> DataComponentType<T> make(ResourceLocation loc, UnaryOperator<DataComponentType.Builder<T>> unaryOperator) {
        DataComponentType<T> type = unaryOperator.apply(DataComponentType.builder()).build();
        COMPONENTS.put(loc, type);

        return type;
    }
}
