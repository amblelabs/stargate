package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.drtheo.ecs.event.TEvent;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.StateResolveError;

public interface IrisEvents extends TEvents {

    Type<IrisEvents> type = new Type<>(IrisEvents.class);

    void iris$onBroken(Stargate stargate, IrisState state);

    record Broken(Stargate stargate, IrisState state) implements TEvent.Notify<IrisEvents> {

        @Override
        public void handle(IrisEvents handler) throws StateResolveError {
            handler.iris$onBroken(stargate, state);
        }

        @Override
        public BaseType<IrisEvents> type() {
            return type;
        }
    }
}
