package dev.amble.stargate.api.v3.behavior.address;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.StargateServerData;
import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.address.AddressResolveEvent;
import dev.amble.stargate.api.v3.event.address.AddressResolveEvents;
import dev.amble.stargate.api.v3.event.address.StargateRemoveEvents;
import dev.amble.stargate.api.v3.state.address.GlobalAddressState;
import dev.amble.stargate.api.v3.state.address.LocalAddressState;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.behavior.TBehaviorRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public interface AddressBehaviors {

    static void registerAll() {
        TBehaviorRegistry.register(C7::new);
        TBehaviorRegistry.register(C8::new);
        TBehaviorRegistry.register(C9::new);

        TBehaviorRegistry.register(LocalAddressBehavior::new);
        TBehaviorRegistry.register(GlobalAddressBehavior::new);
    }

    class LocalAddressBehavior implements TBehavior, StargateRemoveEvents, StargateEvents {

        @Override
        public void remove(StargateServerData data, Stargate stargate) {
            data.removeLocal(address(stargate));
        }

        @Override
        public void onCreated(Stargate stargate) {
            StargateServerData.getOrCreate((ServerWorld) stargate.world())
                    .add(address(stargate), stargate);
        }

        private long address(Stargate stargate) {
            return stargate.state(LocalAddressState.state).address();
        }
    }

    class GlobalAddressBehavior implements TBehavior, StargateRemoveEvents, StargateEvents {

        @Override
        public void remove(StargateServerData data, Stargate stargate) {
            data.removeGlobal(address(stargate));
        }

        @Override
        public void onCreated(Stargate stargate) {
            StargateServerData.getOrCreate((ServerWorld) stargate.world())
                    .add(address(stargate), stargate);
        }

        private long address(Stargate stargate) {
            return stargate.state(GlobalAddressState.state).address();
        }
    }

    class C7 implements TBehavior, AddressResolveEvents {

        public static long COST_PT = 2 * 100_000;

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long targetAddress, int length) {
            if (length != 7) return AddressResolveEvent.PASS;

            long ownAddress = stargate.state(LocalAddressState.state).address();

            char originChar = AddressProvider.Local.getOriginChar(ownAddress);
            char targetChar = AddressProvider.Local.getOriginChar(targetAddress);

            // must be a 9c address...
            if (originChar != targetChar) return AddressResolveEvent.PASS;

            ServerWorld world = (ServerWorld) stargate.world();

            Stargate target = StargateServerData.getOrCreate(world).getLocal(targetAddress);
            return AddressResolveEvent.routeOrFail(target, 0, COST_PT);
        }
    }

    class C8 implements TBehavior, AddressResolveEvents {

        public static long COST_PT = 2 * 1_000_000;

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long targetAddress, int length) {
            if (length != 8) return AddressResolveEvent.PASS;

            long ownAddress = stargate.state(LocalAddressState.state).address();

            char originChar = AddressProvider.Local.getOriginChar(ownAddress);
            char targetChar = AddressProvider.Local.getOriginChar(targetAddress);

            // must be a 9c address...
            if (originChar != targetChar) return AddressResolveEvent.PASS;

            ServerWorld world = ServerLifecycleHooks.get().getWorld(AddressProvider.Local.getTarget(targetAddress));

            if (world == null)
                return AddressResolveEvent.PASS; // do not fail in case it is an incomplete 9c address.

            Stargate target = StargateServerData.getOrCreate(world).getLocal(targetAddress);
            return AddressResolveEvent.routeOrFail(target, 0, COST_PT);
        }
    }

    class C9 implements TBehavior, AddressResolveEvents {

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long targetAddress, int length) {
            if (length != 9) return AddressResolveEvent.PASS;

            ServerWorld world = ServerLifecycleHooks.get().getWorld(AddressProvider.Global.getTarget(targetAddress));

            if (world == null)
                return AddressResolveEvent.FAIL;

            Stargate target = StargateServerData.getOrCreate(world).getGlobal(targetAddress);

            if (target == null)
                return AddressResolveEvent.FAIL;

            // 200 kFE/t + 200 * distance to destination / 2000
            float distance = MathHelper.sqrt((float) target.pos().getSquaredDistance(target.pos()));
            long cost = C7.COST_PT + MathHelper.floor(distance / 10);

            return AddressResolveEvent.routeOrFail(target, 0, cost);
        }
    }
}
