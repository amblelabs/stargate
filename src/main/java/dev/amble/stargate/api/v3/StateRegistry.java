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
    private static boolean frozen;

    public static void register(GateState.Type<?> state) {
        if (frozen) throw new IllegalStateException("Attempted to register a state while the registry was frozen");

        state.key().register(states.size());
        states.add(state);
    }

    public static void register(GateState.Group group) {
        for (GateState.Type<?> type : group.types()) {
            register(type);
        }
    }

    public static void freeze() {
        frozen = true;
    }

    /**
     * Creates a recommended {@link GateStateHolder}. The result depends on the amount of registered state types.
     * @return recommended {@link GateStateHolder}.
     *
     * @see GateStateHolder
     * @see #createArrayHolder()
     * @see #createMapHolder()
     */
    public static GateStateHolder<?> createStateHolder() {
        return states.size() > 100 ? createArrayHolder() : createMapHolder();
    }

    /**
     * Creates an array-backed {@link GateStateHolder} with the size of the frozen registry.
     * @return an array-backed {@link GateStateHolder}.
     *
     * @see GateStateHolder
     */
    public static ArrayStateHolder createArrayHolder() {
        if (!frozen) throw new IllegalStateException("Attempted to create an array state holder with an unfrozen registry!");

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

    public static final class ArrayStateHolder implements GateStateHolder<ArrayStateHolder> {

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
        public void forEachState(@NotNull Consumer<GateState<?>> consumer) {
            for (GateState<?> state : states) {
                if (state == null) continue;

                consumer.accept(state);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends GateState<T>> T removeState(@NotNull GateState.Type<T> type) {
            int idx = type.key().index;

            T result = (T) this.states[idx];
            this.states[idx] = null;

            return result;
        }

        @Override
        public void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state) {
            int idx = type.key().verifyIdx();

            if (this.states[idx] != null)
                return;

            this.states[type.key().verifyIdx()] = state;
        }
    }

    public static final class MapStateHolder implements GateStateHolder<MapStateHolder> {

        private final Int2ObjectOpenHashMap<GateState<?>> states = new Int2ObjectOpenHashMap<>(40);

        private MapStateHolder() {

        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type) {
            return (T) states.get(type.key().index);
        }

        @Override
        public void forEachState(@NotNull Consumer<GateState<?>> consumer) {
            states.values().forEach(consumer);
        }

        @Override
        public <T extends GateState<T>> T removeState(@NotNull GateState.Type<T> type) {
            return (T) states.remove(type.key().index);
        }

        @Override
        @ApiStatus.Internal
        public void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state) {
            states.put(type.key().verifyIdx(), state);
        }
    }
}
