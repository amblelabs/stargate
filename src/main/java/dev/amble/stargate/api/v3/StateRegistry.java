package dev.amble.stargate.api.v3;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StateRegistry {

    private static final List<GateState.Type<?>> states = new ArrayList<>();

    public static void register(GateState.Type<?> state) {
        state.key().index = states.size();
        states.add(state);
    }

    public static void register(GateState.Group group) {
        for (GateState.Type<?> type : group.types()) {
            register(type);
        }
    }

    // TODO: implement freezing
    public void freeze() {

    }

    /**
     * Creates a recommended {@link GateStateHolder}. The result depends on the amount of registered state types.
     * @return recommended {@link GateStateHolder}.
     *
     * @see GateStateHolder
     * @see #createDirectHolder()
     * @see #createMapHolder()
     */
    public static GateStateHolder<?> createStateHolder() {
        return states.size() > 100 ? createDirectHolder() : createMapHolder();
    }

    /**
     * Creates a direct {@link GateStateHolder} (meaning, array-backed).
     * @return an array-backed {@link GateStateHolder}.
     *
     * @see GateStateHolder
     */
    public static ArrayStateHolder createDirectHolder() {
        return new ArrayStateHolder(states.size());
    }

    /**
     * Creates a map {@link GateStateHolder}.
     * @implNote The map used is {@link Int2ObjectOpenHashMap}, as it offers the best performance for int-object entries.
     * @return a map-backed {@link GateStateHolder}.
     *
     * @see GateStateHolder
     * @see Int2ObjectOpenHashMap
     */
    public static MapStateHolder createMapHolder() {
        return new MapStateHolder();
    }

    public static class ArrayStateHolder implements GateStateHolder<ArrayStateHolder> {

        private final GateState<?>[] states;

        private ArrayStateHolder(int size) {
            this.states = new GateState[size];
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type) {
            return (T) states[type.key().index];
        }

        @Override
        public void forEachState(Consumer<GateState<?>> consumer) {
            for (GateState<?> state : states) {
                consumer.accept(state);
            }
        }

        @Override
        public void removeState(@NotNull GateState.Type<?> type) {
            states[type.key().index] = null;
        }

        @Override
        public void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state) {
            states[type.key().index] = state;
        }
    }

    public static class MapStateHolder implements GateStateHolder<MapStateHolder> {

        private final Int2ObjectOpenHashMap<GateState<?>> states = new Int2ObjectOpenHashMap<>(40);

        private MapStateHolder() {

        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type) {
            return (T) states.get(type.key().index);
        }

        @Override
        public void forEachState(Consumer<GateState<?>> consumer) {
            states.values().forEach(consumer);
        }

        @Override
        public void removeState(@NotNull GateState.Type<?> type) {
            states.remove(type.key().index);
        }

        @Override
        @ApiStatus.Internal
        public void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state) {
            states.put(type.key().index, state);
        }
    }
}
