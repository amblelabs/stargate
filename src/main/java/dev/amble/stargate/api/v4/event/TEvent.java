package dev.amble.stargate.api.v4.event;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v4.state.StateResolveError;

import java.util.function.Supplier;

public interface TEvent<T extends TEvents> {

    TEvents.BaseType<T> type();

    void handleAll(Iterable<T> subscribed);

    static void handleSilent(TEvent<?> event, TEvents handler, Runnable r) {
        try {
            r.run();
        } catch (StateResolveError ignored) {
            // ignored
        } catch (Throwable e) {
            StargateMod.LOGGER.error("Failed to handle event '{}' for handler '{}'", event.getClass(), handler.getClass(), e);
        }
    }

    static <R> R handleSilent(TEvent<?> event, TEvents handler, Supplier<R> s, Supplier<R> def) {
        try {
            return s.get();
        } catch (StateResolveError ignored) {
            // ignored
        } catch (Throwable e) {
            StargateMod.LOGGER.error("Failed to handle event '{}' for handler '{}'", event.getClass(), handler.getClass(), e);
        }

        return def.get();
    }

    static <R> R handleSilent(TEvent<?> event, TEvents handler, Supplier<R> s, R def) {
        return handleSilent(event, handler, s, (Supplier<R>) () -> def);
    }

    interface Notify<T extends TEvents> extends TEvent<T> {
        void handle(T handler) throws StateResolveError;

        @Override
        default void handleAll(Iterable<T> subscribed) {
            for (T handler : subscribed) {
                handleSilent(this, handler, () -> this.handle(handler));
            }
        }
    }

    interface Result<T extends TEvents, R> extends TEvent<T> {
        R result();

        @Override
        void handleAll(Iterable<T> subscribed);
    }
}
