package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.event.TEvent;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StargateTpEvent implements TEvent.Result<StargateTpEvent.Callback, StargateTpEvent.Result> {

    public static final TEvents.Type<Callback> event = new TEvents.Type<>(Callback.class);

    @FunctionalInterface
    public interface Callback extends TEvents {
        StargateTpEvent.Result onGateTp(Stargate from, Stargate to, Entity living);
    }

    private final Stargate from;
    private final Stargate to;
    private final Entity living;

    protected Result result = Result.PASS;

    public StargateTpEvent(Stargate from, Stargate to, LivingEntity living) {
        this.from = from;
        this.to = to;
        this.living = living;
    }

    @Override
    public TEvents.BaseType<Callback> type() {
        return event;
    }

    @Override
    public Result result() {
        return result;
    }

    @Override
    public void handleAll(Iterable<Callback> subscribed) {
        for (Callback e : subscribed) {
            Result newRes = TEvent.handleSilent(this, e, () -> this.handle(e), Result.PASS);

            if (newRes == Result.PASS) continue;

            this.result = newRes;
            break;
        }
    }

    public Result handle(Callback handler) {
        return handler.onGateTp(from, to, living);
    }

    public enum Result {
        DENY,
        PASS,
        ALLOW
    }
}