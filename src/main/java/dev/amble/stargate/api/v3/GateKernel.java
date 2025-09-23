package dev.amble.stargate.api.v3;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GateKernel implements GateStateHolder<GateKernel> {

    private final Int2ObjectOpenHashMap<GateState<?>> states = new Int2ObjectOpenHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type) {
        return (T) states.get(type.key().index);
    }

    public <T extends GateState<T>> @NotNull T state(@NotNull GateState.Type<T> type) {
        T result = stateOrNull(type);

        if (result == null)
            throw new ResolveError();

        return result;
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

    public void tick() {
        states.values().forEach(state -> {
            for (GateBehavior<?> behavior : BehaviorRegistry.get(state.type())) {
                behavior.internal$tick(this);
            }
        });
    }
}
