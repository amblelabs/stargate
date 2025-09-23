package dev.amble.stargate.api.v4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GateStateHolder {
    <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type);
    <T extends GateState<T>> @NotNull T state(@NotNull GateState.Type<T> type);

    void removeState(@NotNull GateState.Type<?> type);
    void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state);

    default void internal$addState(@NotNull GateState<?> state) {
        GateState.Type<?> type = state.type();
        GateState.Group group = type.group();

        if (group != null)
            group.removeAll(this);

        this.internal$addState(type, state);
    }
}
