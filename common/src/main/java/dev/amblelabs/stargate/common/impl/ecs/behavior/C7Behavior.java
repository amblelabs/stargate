package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.AddressResolveEvent;
import dev.amblelabs.stargate.api.ecs.event.AddressResolveEvents;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.drtheo.ecs.behavior.TBehavior;

public class C7Behavior implements TBehavior, AddressResolveEvents {

    @Override
    public AddressResolveEvent.Result resolve(Stargate stargate, String targetAddress, int length) {
        Stargate target = ServerStargateNetwork.C7.get(targetAddress);
        if (target == null) return new AddressResolveEvent.Result.Pass();

        return new AddressResolveEvent.Result.Route(target, 0, 0);
    }
}
