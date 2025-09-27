package dev.amble.stargate.api.v3.event;

import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;

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
