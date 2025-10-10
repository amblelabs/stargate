package dev.amble.stargate.api.v3.event.address;

import dev.amble.stargate.api.address.v2.AddressProvider;
import dev.amble.stargate.api.v3.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class AddressResolveEvent implements TEvent.Result<AddressResolveEvents, AddressResolveEvent.Result> {

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
    public Result result() {
        return result;
    }

    public interface Result {

        default Result ifFail(Runnable runnable) {
            return this;
        }

        default Result ifPass(Runnable runnable) {
            return this;
        }

        default Result ifRoute(Consumer<Route> consumer) {
            return this;
        }

        Result PASS = new Result() {
            @Override
            public Result ifPass(Runnable runnable) {
                runnable.run();
                return this;
            }
        };
        Result FAIL = new Result() {
            @Override
            public Result ifFail(Runnable runnable) {
                runnable.run();
                return this;
            }
        };

        static Result route(@NotNull Stargate stargate, long openCost, long costPerTick) {
            return new Route(stargate, openCost, costPerTick);
        }

        static Result routeOrFail(@Nullable Stargate stargate, long openCost, long costPerTick) {
            return stargate == null ? FAIL : route(stargate, openCost, costPerTick);
        }

        record Route(Stargate stargate, long openCost, long costPerTick) implements Result {

            @Override
            public Result ifRoute(Consumer<Route> consumer) {
                consumer.accept(this);
                return this;
            }
        }
    }
}
