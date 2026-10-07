package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

// TODO: use EventSingle
public interface StargateLifecycleEvents extends TEvents {

    EventGroup<StargateLifecycleEvents> type = new EventGroup<>(StargateLifecycleEvents.class);

    void stargate$instantiate(Stargate stargate, NbtDeserializer.Context ctx);
}
