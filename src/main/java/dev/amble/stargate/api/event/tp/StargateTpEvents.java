package dev.amble.stargate.api.event.tp;

import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.entity.LivingEntity;

public interface StargateTpEvents extends TEvents {

    Type<StargateTpEvents> event = new Type<>(StargateTpEvents.class);

    StargateTpEvent.Result onGateTp(Stargate from, Stargate to, LivingEntity living);
}
