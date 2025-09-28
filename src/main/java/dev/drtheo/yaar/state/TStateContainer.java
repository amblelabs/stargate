package dev.drtheo.yaar.state;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface that represents a {@link TState} container.
 */
public interface TStateContainer {

    /**
     * Gets the queried {@link TState} by its {@link TState.Type} or {@code null}.
     *
     * @param type the {@link TState}'s type.
     * @return queried {@link TState} or {@code null}.
     * @param <T> the state.
     */
    @Contract(pure = true)
    <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type);

    /**
     * Utility method that gets the queried {@link TState}
     * by its {@link TState.Type} or exits early if it wasn't found.
     *
     * @param type the {@link TState}'s type.
     * @return the queried state's instance.
     * @param <T> the state.
     * @throws StateResolveError if the state is not found. Handled internally (most of the time).
     * @see StateResolveError
     */
    @Contract(pure = true)
    default <T extends TState<T>> @NotNull T state(@NotNull TState.Type<T> type) {
        T result = stateOrNull(type);

        if (result == null)
            throw StateResolveError.create(this, type);

        return result;
    }

    /**
     * Removes the {@link TState} by its {@link TState.Type}.
     *
     * @param type the {@link TState}'s type.
     * @return the removed {@link TState}, or null.
     * @param <T> the state.
     */
    @Contract(mutates = "this")
    <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type);

    /**
     * Adds the {@link TState}.
     * @implNote Fails if the state is already present.
     *
     * @param state the {@link TState} to add.
     * @return whether adding the state had succeeded.
     */
    @Contract(mutates = "this")
    boolean addState(@NotNull TState<?> state);

    /**
     * A basic implementation of {@link TStateContainer} that's backed by an array.
     */
    class ArrayBacked implements TStateContainer {

        private final TState<?>[] data;

        /**
         * @param maxSize the maximum size of the array.
         */
        @Contract(pure = true)
        protected ArrayBacked(int maxSize) {
            data = new TState[maxSize];
        }

        @Override
        @Contract(pure = true)
        @SuppressWarnings("unchecked")
        public <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type) {
            return (T) data[type.index];
        }

        @Override
        @Contract(mutates = "this")
        @SuppressWarnings("unchecked")
        public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
            T result = (T) data[type.index];
            data[type.index] = null;

            return result;
        }

        @Override
        @Contract(mutates = "this")
        public boolean addState(@NotNull TState<?> state) {
            int index = state.type().verifyIndex();
            data[index] = state;
            return true;
        }
    }

    /**
     * A delegate implementation of {@link TStateContainer} that delegates all the work to a parent {@link TStateContainer}.
     * @apiNote extend this class for usage.
     */
    abstract class Delegate implements TStateContainer {

        private final TStateContainer parent;

        /**
         * @param container the parent container to delegate {@link TStateContainer} calls to.
         */
        @Contract(pure = true)
        public Delegate(TStateContainer container) {
            this.parent = container;
        }

        @Override
        @Contract(pure = true)
        public <T extends TState<T>> @Nullable T stateOrNull(@NotNull TState.Type<T> type) {
            return parent.stateOrNull(type);
        }

        @Override
        @Contract(mutates = "this")
        public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
            return parent.removeState(type);
        }

        @Override
        @Contract(mutates = "this")
        public boolean addState(@NotNull TState<?> state) {
            return parent.addState(state);
        }
    }
}
