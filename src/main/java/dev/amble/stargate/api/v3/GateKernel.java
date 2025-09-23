package dev.amble.stargate.api.v3;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class GateKernel extends DelegateStateHolder<GateKernel> {

    public void tick() {
        this.forEachState(this::tickBehavior);
    }

    @Override
    public void internal$addState(GateState.@NotNull Type<?> type, @NotNull GateState<?> state) {
        super.internal$addState(type, state);
        this.forEachState(this::initBehavior);
    }

    @Override
    public void removeState(GateState.@NotNull Type<?> type) {
        super.removeState(type);
        this.forEachState(this::removeBehavior);
    }

    private <T extends GateState<T>> void tickBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.tick(this, t));
    }

    private <T extends GateState<T>> void initBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.init(this, t));
    }

    private <T extends GateState<T>> void removeBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.finish(this, t));
    }

    private <T extends GateState<T>> void runEvent(GateState<T> state, BiConsumer<GateBehavior<T>, T> consumer) {
        for (GateBehavior<T> behavior : BehaviorRegistry.get(state.type())) {
            consumer.accept(behavior, state.unbox());
        }
    }
}
