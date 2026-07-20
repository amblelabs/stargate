package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

import java.util.function.Consumer;

public interface StargateLifecycleEvents extends TEvents {

    Type<StargateLifecycleEvents> type = new Type<>(StargateLifecycleEvents.class);

    void stargate$instantiate(Stargate stargate, NbtDeserializer.Context ctx);

    static void notify(Consumer<StargateLifecycleEvents> handler) {
        TEvents.notify(type, handler);
    }
}
