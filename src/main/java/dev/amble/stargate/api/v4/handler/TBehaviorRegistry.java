package dev.amble.stargate.api.v4.handler;

import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.event.TEventsRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

public class TBehaviorRegistry {

    private static boolean frozen;
    private static final Set<TBehavior> handlers = Collections.newSetFromMap(new IdentityHashMap<>());

    public static void register(TBehavior handler) {
        if (frozen)
            throw new IllegalStateException("Already frozen");

        if (!TEventsRegistry.isFrozen())
            throw new IllegalStateException("Events registry was not frozen yet!");

        handlers.add(handler);
        buildEvents(handler);
    }

    public static void register(Supplier<TBehavior> handler) {
        register(handler.get());
    }

    public static void freeze() {
        frozen = true;

        Map<Class<?>, TBehavior> lookup = new HashMap<>();

        for (TBehavior handler : handlers) {
            lookup.put(handler.getClass(), handler);
        }

        for (TBehavior handler : handlers) {
            Class<? extends TBehavior> clazz = handler.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                if (!Modifier.isFinal(field.getModifiers()))
                    continue;

                Resolve resolve = field.getAnnotation(Resolve.class);

                if (resolve == null)
                    continue;

                Class<?> target = field.getType();

                try {
                    field.set(handler, lookup.get(target));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void buildEvents(TBehavior handler) {
        for (TEvents.BaseType<?> holder : TEventsRegistry.registered()) {
            if (!holder.isApplicable(handler))
                continue;

            holder.subscribe(handler);
        }
    }
}
