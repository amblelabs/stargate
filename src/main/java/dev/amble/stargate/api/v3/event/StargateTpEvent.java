package dev.amble.stargate.api.v3.event;

import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.entity.LivingEntity;

public class StargateTpEvent implements TEvent.Result<StargateEvents, StargateTpEvent.Result> {

    private final Stargate from;
    private final Stargate to;
    private final LivingEntity living;
    private Result result = Result.ALLOW;

    public StargateTpEvent(Stargate from, Stargate to, LivingEntity living) {
        this.from = from;
        this.to = to;
        this.living = living;
    }

    @Override
    public Result result() {
        return result;
    }

    @Override
    public TEvents.BaseType<StargateEvents> type() {
        return StargateEvents.event;
    }

    @Override
    public void handleAll(Iterable<StargateEvents> subscribed) {
        for (StargateEvents e : subscribed) {
            Result newRes = TEvent.handleSilent(this, e, () -> e.onGateTp(from, to, living), Result.ALLOW);

            if (newRes == Result.ALLOW) continue;

            this.result = newRes;
            break;
        }
    }

    public enum Result {
        DENY,
        ALLOW
    }
}
