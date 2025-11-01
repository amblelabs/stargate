package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;

public record StargateAnimateEvent(StargateBlockEntity stargateBlockEntity, Stargate stargate, BaseStargateModel model, int age) implements TEvent.Notify<StargateAnimateEvents> {

    @Override
    public TEvents.BaseType<StargateAnimateEvents> type() {
        return StargateAnimateEvents.event;
    }

    @Override
    public void handle(StargateAnimateEvents handler) throws StateResolveError {
        handler.animate(stargateBlockEntity, stargate, model, age);
    }
}
