package dev.amble.stargate.client.api.event.render;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.state.stargate.ClientAbstractStargateState;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.util.profiler.Profiler;

public interface StargateAnimateEvents extends TEvents {

    Type<StargateAnimateEvents> event = new Type<>(StargateAnimateEvents.class);

    void animate(StargateBlockEntity block, Stargate stargate, ClientAbstractStargateState<?> state, Profiler profiler);
}
