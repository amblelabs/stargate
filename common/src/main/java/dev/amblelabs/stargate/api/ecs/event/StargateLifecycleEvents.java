package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvent;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.StateResolveError;

public interface StargateLifecycleEvents extends TEvents {

    Type<StargateLifecycleEvents> type = new Type<>(StargateLifecycleEvents.class);

    void stargate$instantiate(Stargate stargate, NbtDeserializer.Context ctx);

    record Instantiate(Stargate stargate, NbtDeserializer.Context ctx) implements TEvent.Notify<StargateLifecycleEvents> {

        @Override
        public BaseType<StargateLifecycleEvents> type() {
            return type;
        }

        @Override
        public void handle(StargateLifecycleEvents handler) throws StateResolveError {
            handler.stargate$instantiate(stargate, ctx);
        }
    }
}
