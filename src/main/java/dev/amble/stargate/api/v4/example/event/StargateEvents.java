package dev.amble.stargate.api.v4.example.event;

import dev.amble.stargate.api.v4.data.TState;
import dev.amble.stargate.api.v4.event.TEvents;
import dev.amble.stargate.api.v4.example.Stargate;

public interface StargateEvents extends TEvents {

    Type<StargateEvents> event = new Type<>(StargateEvents.class);

    default void onCreated(Stargate stargate) { }

    default void onStateChanged(Stargate stargate, TState<?> oldState, TState<?> newState) { }

    default void tick(Stargate stargate) { }

    interface State extends TEvents {

        Type<State> event = new Type<>(State.class);

        default void onStateAdded(Stargate stargate, TState<?> state) { }

        default void onStateRemoved(Stargate stargate, TState<?> state) { }
    }
}
