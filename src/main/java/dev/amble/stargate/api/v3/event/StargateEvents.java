package dev.amble.stargate.api.v3.event;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.GateState;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;
import net.minecraft.entity.LivingEntity;

public interface StargateEvents extends TEvents {

    Type<StargateEvents> event = new Type<>(StargateEvents.class);

    default void onCreated(Stargate stargate) { }

    default void onStateChanged(Stargate stargate, GateState<?> oldState, GateState<?> newState) { }

    default void tick(Stargate stargate) { }

    default StargateTpEvent.Result onGateTp(Stargate from, Stargate to, LivingEntity living) { return StargateTpEvent.Result.ALLOW; }

    interface State extends TEvents {

        Type<State> event = new Type<>(State.class);

        default void onStateAdded(Stargate stargate, TState<?> state) { }

        default void onStateRemoved(Stargate stargate, TState<?> state) { }
    }
}
