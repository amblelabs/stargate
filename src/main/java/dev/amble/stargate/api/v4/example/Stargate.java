package dev.amble.stargate.api.v4.example;

import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.event.StargateCreatedEvent;
import dev.amble.stargate.api.v4.example.event.state.StateAddedEvent;
import dev.amble.stargate.api.v4.example.event.state.StateRemovedEvent;
import dev.amble.stargate.api.v4.example.state.GateState;
import dev.amble.stargate.api.v4.state.TState;
import dev.amble.stargate.api.v4.state.TStateContainer;
import dev.amble.stargate.api.v4.state.TStateRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Stargate extends TStateContainer.Delegate {

    public Stargate(boolean created, @NotNull Supplier<TState<?>> defState, @NotNull TState.Type<?>... states) {
        super(TStateRegistry.createArrayHolder());

        this.addState(new GateState(this, defState, states));

        if (created) TEvents.handle(new StargateCreatedEvent(this));
    }

    @Override
    public boolean addState(@NotNull TState<?> state) {
        boolean result = super.addState(state);

        if (result)
            TEvents.handle(new StateAddedEvent(this, state));

        return result;
    }

    @Override
    public <T extends TState<T>> @Nullable T removeState(@NotNull TState.Type<T> type) {
        T result = super.removeState(type);

        if (result != null)
            TEvents.handle(new StateRemovedEvent(this, result));

        return result;
    }
}
