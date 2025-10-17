package dev.amble.stargate.api.gates.event.state;

import dev.amble.stargate.api.gates.Stargate;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;

public interface StargateTStateEvents extends TEvents {

    Type<StargateTStateEvents> event = new Type<>(StargateTStateEvents.class);

    void onStateAdded(Stargate stargate, TState<?> state);

    void onStateRemoved(Stargate stargate, TState<?> state);
}
