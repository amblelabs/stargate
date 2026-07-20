package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;

import java.util.function.Consumer;

public interface StargateTickEvents extends TEvents {
    Type<StargateTickEvents> type = new Type<>(StargateTickEvents.class);

    void tick(Stargate someGate);

    static void notify(Consumer<StargateTickEvents> handler) {
        TEvents.notify(type, handler);
    }
}
