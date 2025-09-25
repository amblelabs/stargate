package dev.amble.stargate.api.v4.event;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class TEventsRegistry {

    private static boolean frozen;
    private static final Set<TEvents.BaseType<?>> holders = Collections.newSetFromMap(new IdentityHashMap<>());

    public static void register(TEvents.BaseType<?> events) {
        if (frozen)
            throw new IllegalStateException("Already frozen");

        holders.add(events);
    }

    public static Collection<TEvents.BaseType<?>> registered() {
        return holders;
    }

    public static void freeze() {
        frozen = true;
    }

    public static boolean isFrozen() {
        return frozen;
    }
}
