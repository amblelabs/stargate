package dev.amble.stargate.api.v3;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

public class BehaviorRegistry {

    private static final Int2ObjectMap<TypedList<?>> REGISTRY = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends GateState<T>> void register(GateBehavior<T> behavior) {
        TypedList<T> list = (TypedList<T>) REGISTRY.computeIfAbsent(behavior.type().key().index, i -> new TypedList<T>());
        list.add(behavior);
    }

    @SuppressWarnings("unchecked")
    public static <T extends GateState<T>> TypedList<T> get(GateState.Type<T> type) {
        return (TypedList<T>) REGISTRY.get(type.key().index);
    }

    public static final class TypedList<T extends GateState<T>> extends ArrayList<GateBehavior<T>> { }
}
