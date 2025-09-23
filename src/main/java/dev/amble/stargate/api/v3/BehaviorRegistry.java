package dev.amble.stargate.api.v3;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

public class BehaviorRegistry {

    private static final Int2ObjectMap<BehaviorList<?>> REGISTRY = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends GateState<T>> void register(GateBehavior<T> behavior) {
        ((BehaviorList<T>) REGISTRY.computeIfAbsent(behavior.type().key().verifyIdx(),
                i -> new BehaviorList<T>())).add(behavior);
    }

    @SuppressWarnings("unchecked")
    public static <T extends GateState<T>> BehaviorList<T> get(GateState.Type<T> type) {
        return (BehaviorList<T>) REGISTRY.get(type.key().index);
    }

    public static final class BehaviorList<T extends GateState<T>> extends ArrayList<GateBehavior<T>> { }
}
