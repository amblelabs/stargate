package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.world.entity.Entity;

public interface StargateTpEvents extends TEvents {

    TEvents.Type<StargateTpEvents> type = new TEvents.Type<>(StargateTpEvents.class);

    StargateTpEvent.Result onGateTp(Stargate from, Stargate to, Entity living);
}