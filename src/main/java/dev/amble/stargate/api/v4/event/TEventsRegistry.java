package dev.amble.stargate.api.v4.event;

import dev.amble.stargate.api.v4.behavior.TBehavior;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Registry for all {@link TEvents} (event groups, not {@link TEvent}).
 * All elements must be registered and the registry should be frozen <i>before</i> registering {@link TBehavior}s.
 */
public class TEventsRegistry {

    private static boolean frozen;
    private static final Set<TEvents.BaseType<?>> holders = Collections.newSetFromMap(new IdentityHashMap<>());

    /**
     * Registers the provided event group type.
     *
     * @param events the event group type.
     * @throws IllegalStateException if the registry is already frozen.
     */
    public static void register(TEvents.BaseType<?> events) {
        if (frozen)
            throw new IllegalStateException("Registry already frozen!");

        holders.add(events);
    }

    /**
     * Returns a collection of all the registered event group types (cached).
     *
     * @return a collection of all the registered {@link TEvents.BaseType}s.
     */
    public static Collection<TEvents.BaseType<?>> registered() {
        return holders;
    }

    /**
     * Freezes the registry, allowing for {@link TBehavior} registration.
     */
    public static void freeze() {
        frozen = true;
    }

    /**
     * @return whether the registry is frozen.
     */
    public static boolean isFrozen() {
        return frozen;
    }
}
