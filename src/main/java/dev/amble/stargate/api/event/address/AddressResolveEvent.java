package dev.amble.stargate.api.event.address;

import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddressResolveEvent implements TEvent.Result<AddressResolveEvents, AddressResolveEvent.Result> {

    private final Stargate stargate;
    private final long address;
    private final int length;

    private Result result = PASS;

    public AddressResolveEvent(Stargate stargate, String address, int length) {
        this.stargate = stargate;
        this.address = AddressProvider.pack(address, length);
        this.length = length;
    }

    @Override
    public TEvents.BaseType<AddressResolveEvents> type() {
        return AddressResolveEvents.event;
    }

    @Override
    public void handleAll(Iterable<AddressResolveEvents> subscribed) {
        for (AddressResolveEvents e : subscribed) {
            Result result = TEvent.handleSilent(this, e, () -> e.resolve(stargate, address, length), PASS);

            if (result == PASS) continue;

            this.result = result;
            return;
        }
    }

    @Override
    public Result result() {
        return result;
    }

    public static final Result PASS = new Result.Pass();
    public static final Result FAIL = new Result.Fail();

    public static Result route(@NotNull Stargate stargate, long openCost, long costPerTick) {
        return new Result.Route(stargate, openCost, costPerTick);
    }

    public static Result routeOrFail(@Nullable Stargate stargate, long openCost, long costPerTick) {
        return stargate == null ? FAIL : route(stargate, openCost, costPerTick);
    }

    public sealed interface Result {

        record Fail() implements Result { }

        record Route(Stargate stargate, long openCost, long costPerTick) implements Result { }

        record Pass() implements Result { }
    }
}
