package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.event.TResult;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface StargateTpEvent extends TEvents {

    EventSingle<StargateTpEvent> type = new EventSingle<>(StargateTpEvent.class, callbacks
            -> (from, to, living) -> {
        TResult result = TResult.ALLOW;
        for (StargateTpEvent cb : callbacks) {
            TResult newRes = cb.stargate$tp(from, to, living);

            if (newRes == TResult.PASS)
                continue;

            result = newRes;
        }

        return result;
    });

    TResult stargate$tp(Stargate from, Stargate to, Entity living);
}