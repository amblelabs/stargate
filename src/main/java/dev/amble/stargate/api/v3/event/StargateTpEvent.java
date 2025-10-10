package dev.amble.stargate.api.v3.event;

import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.event.impl.TriStateTEvent;
import net.minecraft.entity.LivingEntity;

public class StargateTpEvent extends TriStateTEvent<StargateEvents> {

    private final Stargate from;
    private final Stargate to;
    private final LivingEntity living;

    public StargateTpEvent(Stargate from, Stargate to, LivingEntity living) {
        this.from = from;
        this.to = to;
        this.living = living;
    }

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }

    @Override
    public Result handle(StargateEvents handler) {
        return handler.onGateTp(from, to, living);
    }
}
