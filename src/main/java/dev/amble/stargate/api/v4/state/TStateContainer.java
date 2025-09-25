package dev.amble.stargate.api.v4.state;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TStateContainer {

    <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type);

    default <T extends TState<T>> @NotNull T state(@NotNull TState.Type<T> type) {
        T result = stateOrNull(type);

        if (result == null)
            throw StateResolveError.create();

        return result;
    }

    <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type);

    boolean addState(@NotNull TState<?> state);

    class ListBacked implements TStateContainer {

        private final TState<?>[] data;

        public ListBacked(int maxSize) {
            data = new TState[maxSize];
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type) {
            return (T) data[type.index];
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
            T result = (T) data[type.index];
            data[type.index] = null;

            return result;
        }

        @Override
        public boolean addState(@NotNull TState<?> state) {
            int index = state.type().verifyIndex();

            if (data[index] != null)
                return false;

            data[index] = state;
            return true;
        }
    }

    class Delegate implements TStateContainer {

        private final TStateContainer parent;

        public Delegate(TStateContainer container) {
            this.parent = container;
        }

        @Override
        public <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type) {
            return parent.stateOrNull(type);
        }

        @Override
        public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
            return parent.removeState(type);
        }

        @Override
        public boolean addState(@NotNull TState<?> state) {
            return parent.addState(state);
        }
    }
}
