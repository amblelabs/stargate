package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.state.stargate.ClientAbstractStargateState;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import net.minecraft.util.profiler.Profiler;

public record StargateAnimateEvent(StargateBlockEntity stargateBlockEntity, Stargate stargate, ClientAbstractStargateState<?> state, Profiler profiler) implements TEvent.Notify<StargateAnimateEvents> {

    @Override
    public TEvents.BaseType<StargateAnimateEvents> type() {
        return StargateAnimateEvents.event;
    }

    @Override
    public void handle(StargateAnimateEvents handler) throws StateResolveError {
        handler.animate(stargateBlockEntity, stargate, state, profiler);
    }
}
