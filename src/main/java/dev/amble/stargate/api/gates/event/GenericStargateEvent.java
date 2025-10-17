package dev.amble.stargate.api.gates.event;

import dev.amble.stargate.api.gates.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

import java.util.function.BiConsumer;

public record GenericStargateEvent<T extends TEvents>(TEvents.BaseType<T> type, Stargate stargate, BiConsumer<T, Stargate> func) implements TEvent.Notify<T> {

    @Override
    public void handle(T t) throws StateResolveError {
        func.accept(t, stargate);
    }
}
