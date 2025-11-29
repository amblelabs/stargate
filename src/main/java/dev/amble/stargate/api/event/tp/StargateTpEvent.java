package dev.amble.stargate.api.event.tp;

import dev.amble.stargate.api.ServerStargate;
import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.event.impl.TriStateTEvent;
import net.minecraft.entity.LivingEntity;

public class StargateTpEvent extends TriStateTEvent<StargateTpEvents> {

    private final ServerStargate from;
    private final ServerStargate to;
    private final LivingEntity living;

    public StargateTpEvent(ServerStargate from, ServerStargate to, LivingEntity living) {
        this.from = from;
        this.to = to;
        this.living = living;
    }

    @Override
    public TEvents.BaseType<StargateTpEvents> type() {
        return StargateTpEvents.event;
    }

    @Override
    public Result handle(StargateTpEvents handler) {
        return handler.onGateTp(from, to, living);
    }
}
