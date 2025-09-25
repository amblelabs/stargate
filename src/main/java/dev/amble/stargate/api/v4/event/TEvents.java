package dev.amble.stargate.api.v4.event;

import dev.amble.stargate.api.v4.handler.TBehavior;

import java.util.ArrayList;
import java.util.List;

public interface TEvents {

    static <T extends TEvents> void handle(TEvent<T> event) {
        event.type().handle(event);
    }

    static <T extends TEvents, R> R handle(TEvent.Result<T, R> event) {
        event.type().handle(event);
        return event.result();
    }

    interface BaseType<T extends TEvents> {
        boolean isApplicable(TBehavior handler);

        void subscribe(TBehavior handler);
        void handle(TEvent<T> event);
    }

    record Type<T extends TEvents>(Class<T> clazz, List<T> handlers) implements BaseType<T> {

        public Type(Class<T> clazz) {
            this(clazz, new ArrayList<>());
        }

        @Override
        @SuppressWarnings("unchecked")
        public void subscribe(TBehavior handler) {
            if (!this.isApplicable(handler))
                throw new IllegalArgumentException("you're crazy");

            handlers.add((T) handler);
        }

        @Override
        public void handle(TEvent<T> event) {
            event.handleAll(handlers);
        }

        @Override
        public boolean isApplicable(TBehavior handler) {
            return clazz.isInstance(handler);
        }
    }
}
