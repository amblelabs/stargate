package dev.amble.stargate.api.v3.event.address;

import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AddressResolveEvent implements TEvent.Result<AddressResolveEvents, Optional<AddressResolveEvent.Result>> {

    private final Stargate stargate;
    private final long address;

    private Result result;

    public AddressResolveEvent(Stargate stargate, String address) {
        this.stargate = stargate;
        this.address = AddressProvider.pack(address);
    }

    @Override
    public TEvents.BaseType<AddressResolveEvents> type() {
        return AddressResolveEvents.event;
    }

    @Override
    public void handleAll(Iterable<AddressResolveEvents> subscribed) {
        int length = AddressProvider.length(address);

        for (AddressResolveEvents e : subscribed) {
            Result result = e.resolve(stargate, address, length);

            if (result == null) continue;

            this.result = result;
            return;
        }
    }

    @Override
    public Optional<Result> result() {
        return Optional.ofNullable(result);
    }

    public interface Result {

        Type type();

        enum Type {
            FAIL,
            ROUTE;
        }

        Result PASS = null;
        Result FAIL = () -> Type.FAIL;

        static Result route(@NotNull Stargate stargate, long openCost, long costPerTick) {
            return new Route(stargate, openCost, costPerTick);
        }

        static Result routeOrFail(@Nullable Stargate stargate, long openCost, long costPerTick) {
            return stargate == null ? FAIL : route(stargate, openCost, costPerTick);
        }

        record Empty(Type type) implements Result { }

        record Route(Stargate stargate, long openCost, long costPerTick) implements Result {

            @Override
            public Type type() {
                return Type.FAIL;
            }
        }
    }
}
