package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.ecs.event.StargateEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.behavior.TBehavior;

public class PrototypeBehavior implements TBehavior {

    public PrototypeBehavior() {
        subscribe(StargateEvents.Instantiate.event, this::onInstantiate);
    }

    public void onInstantiate(Stargate stargate, NbtDeserializer.Context ctx) {
        PrototypeIdentityState identity = stargate.resolveState(PrototypeIdentityState.type);
        identity.prototype().make(identity.key(), StargateEcs.States, stargate, ctx);
    }
}
