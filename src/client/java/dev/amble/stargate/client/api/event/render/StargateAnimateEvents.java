package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.drtheo.yaar.event.TEvents;

public interface StargateAnimateEvents extends TEvents {

    Type<StargateAnimateEvents> event = new Type<>(StargateAnimateEvents.class);

    void animate(StargateBlockEntity stargateBlockEntity, Stargate stargate, BaseStargateModel model, int age);
}
