package dev.amble.stargate.api.v3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class DelegateStateHolder<Self extends DelegateStateHolder<Self>> implements GateStateHolder<Self> {

    protected final GateStateHolder<?> stateHolder;

    public DelegateStateHolder() {
        this(StateRegistry.createArrayHolder());
    }

    public DelegateStateHolder(GateStateHolder<?> holder) {
        this.stateHolder = holder;
    }

    @Override
    public <T extends GateState<T>> @Nullable T stateOrNull(@NotNull GateState.Type<T> type) {
        return stateHolder.stateOrNull(type);
    }

    @Override
    public void forEachState(@NotNull Consumer<GateState<?>> consumer) {
        stateHolder.forEachState(consumer);
    }

    @Override
    public <T extends GateState<T>> T removeState(@NotNull GateState.Type<T> type) {
        return stateHolder.removeState(type);
    }

    @Override
    public void internal$addState(@NotNull GateState.Type<?> type, @NotNull GateState<?> state) {
        stateHolder.internal$addState(type, state);
    }
}
