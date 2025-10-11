package dev.amble.stargate.api.v3.behavior.address;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.address.AddressResolveEvent;
import dev.amble.stargate.api.v3.event.address.AddressResolveEvents;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public interface AddressBehaviors extends TBehavior, AddressResolveEvents {

    static void registerAll() {
        TBehaviorRegistry.register(C7::new);
        TBehaviorRegistry.register(C8::new);
        TBehaviorRegistry.register(C9::new);
    }

    class C7 implements AddressBehaviors {

        public static long COST_PT = 2 * 100_000;

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long address, int length) {
            if (length != 7) return AddressResolveEvent.PASS;

            ServerWorld world = (ServerWorld) stargate.world();

            Stargate target = StargateServerData.getOrCreate(world).getLocal(address);
            return AddressResolveEvent.routeOrFail(target, 0, COST_PT);
        }
    }

    class C8 implements AddressBehaviors {

        public static long COST_PT = 2 * 1_000_000;

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long address, int length) {
            if (length != 8) return AddressResolveEvent.PASS;

            ServerWorld world = ServerLifecycleHooks.get().getWorld(AddressProvider.Local.getTarget(address));

            if (world == null)
                return AddressResolveEvent.FAIL;

            Stargate target = StargateServerData.getOrCreate(world).getLocal(address);
            return AddressResolveEvent.routeOrFail(target, 0, COST_PT);
        }
    }

    class C9 implements AddressBehaviors {

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long address, int length) {
            if (length != 9) return AddressResolveEvent.PASS;

            ServerWorld world = ServerLifecycleHooks.get().getWorld(AddressProvider.Global.getTarget(address));

            if (world == null)
                return AddressResolveEvent.FAIL;

            Stargate target = StargateServerData.getOrCreate(world).getGlobal(address);

            if (target == null)
                return AddressResolveEvent.FAIL;

            // 200 kFE/t + 200 * distance to destination / 2000
            float distance = MathHelper.sqrt((float) target.pos().getSquaredDistance(target.pos()));
            long cost = C7.COST_PT + MathHelper.floor(distance / 10);

            return AddressResolveEvent.routeOrFail(target, 0, cost);
        }
    }
}
