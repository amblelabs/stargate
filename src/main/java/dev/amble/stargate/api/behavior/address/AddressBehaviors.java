package dev.amble.stargate.api.behavior.address;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.stargate.api.data.StargateServerData;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.address.AddressIdsListEvents;
import dev.amble.stargate.api.event.init.StargateCreatedEvents;
import dev.amble.stargate.api.event.address.AddressResolveEvent;
import dev.amble.stargate.api.event.address.AddressResolveEvents;
import dev.amble.stargate.api.event.address.StargateRemoveEvents;
import dev.amble.stargate.api.state.address.GlobalAddressState;
import dev.amble.stargate.api.state.address.LocalAddressState;
import dev.amble.stargate.api.state.stargate.C8Gates;
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

    class LocalAddressBehavior implements TBehavior, StargateRemoveEvents, StargateCreatedEvents, AddressIdsListEvents {

        @Override
        public void remove(StargateServerData data, Stargate stargate) {
            data.removeLocal(stargate.state(LocalAddressState.state).address());
        }

        @Override
        public void onCreated(Stargate stargate) {
            StargateServerData data = StargateServerData.getOrCreate((ServerWorld) stargate.world());
            LocalAddressState address = data.generateAddress(stargate.dimension(), stargate.pos(), LocalAddressState::new);

            stargate.addState(address);

            // FIXME: this causes a sync. a double sync because of globaladdressbehavior, in fact.
            // TODO: make StargateServerData#add use AddressIdsListEvent
            data.addLocal(address.address(), stargate);
        }

        @Override
        public long findAddressId(Stargate stargate) {
            return stargate.state(LocalAddressState.state).getAsLong();
        }
    }

    class GlobalAddressBehavior implements TBehavior, StargateRemoveEvents, StargateCreatedEvents, AddressIdsListEvents {

        @Override
        public void remove(StargateServerData data, Stargate stargate) {
            data.removeGlobal(stargate.globalAddress());
        }

        @Override
        public void onCreated(Stargate stargate) {
            StargateServerData data = StargateServerData.getOrCreate((ServerWorld) stargate.world());
            GlobalAddressState address = data.generateAddress(stargate.dimension(), stargate.pos(), GlobalAddressState::new);

            stargate.addState(address);
            data.addGlobal(address.address(), stargate);
        }

        @Override
        public long findAddressId(Stargate stargate) {
            return stargate.state(GlobalAddressState.state).getAsLong();
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

            if (target == null || stargate.kernel().getClass() != target.kernel().getClass())
                return AddressResolveEvent.FAIL;

            return AddressResolveEvent.route(target, 0, COST_PT);
        }
    }

    class C8 implements TBehavior, AddressResolveEvents {

        public static long COST_PT = 2 * 1_000_000;

        @Override
        public AddressResolveEvent.Result resolve(Stargate stargate, long targetAddress, int length) {
            if (length != 8 || !(stargate.kernel() instanceof C8Gates)) return AddressResolveEvent.PASS;

            long ownAddress = stargate.state(LocalAddressState.state).address();

            char originChar = AddressProvider.Local.getOriginChar(ownAddress);
            char targetChar = AddressProvider.Local.getOriginChar(targetAddress);

            // must be a 9c address...
            if (originChar != targetChar) return AddressResolveEvent.PASS;

            ServerWorld world = ServerLifecycleHooks.get().getWorld(AddressProvider.Local.getTarget(targetAddress));

            if (world == null)
                return AddressResolveEvent.PASS; // do not fail in case it is an incomplete 9c address.

            Stargate target = StargateServerData.getOrCreate(world).getLocal(targetAddress);

            if (target == null || !(target.kernel() instanceof C8Gates))
                return AddressResolveEvent.FAIL;

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
