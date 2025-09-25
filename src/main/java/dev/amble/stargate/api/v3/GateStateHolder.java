package dev.amble.stargate.api.v3;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface GateStateHolder<Self extends GateStateHolder<Self>> {
    <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type);

    default <T extends GateState<T>> @NotNull T state(@NotNull GateState.Type<T> type) {
        T result = stateOrNull(type);

        if (result == null)
            throw new ResolveError();

        return result;
    }

    void forEachState(@NotNull Consumer<GateState<?>> consumer);

    @Nullable
    <T extends GateState<T>> T removeState(@NotNull GateState.Type<T> type);

    @ApiStatus.Internal
    void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state);

    default void addState(@NotNull GateState<?> state) {
        GateState.Type<?> type = state.type();
        GateState.Group group = type.group();

        if (group != null)
            group.removeAll(this);

        this.internal$addState(type, state);
    }

    @SuppressWarnings("unchecked")
    default void doSafe(ThrowableConsumer<Self> consumer) {
        try {
            consumer.apply((Self) this);
        } catch (Throwable ignored) { }
    }

    class ResolveError extends Error {

        public ResolveError() {
            super("If you see this message, then that means someone forgot to use #doSafe!");
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
