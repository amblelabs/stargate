package dev.amble.stargate.api.event;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

import java.util.function.BiConsumer;

public record GenericStargateEvent<T extends TEvents, S extends Stargate>(TEvents.BaseType<T> type, S stargate, BiConsumer<T, S> func) implements TEvent.Notify<T> {

    @Override
    public void handle(T t) throws StateResolveError {
        func.accept(t, stargate);
    }
}
