package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.drtheo.ecs.event.TEvents;

public interface IrisEvents {

    @FunctionalInterface
    interface Break extends TEvents {
        EventSingle<Break> event = new EventSingle<>(Break.class);

        void iris$onBroken(Stargate stargate, IrisState state);
    }
}
