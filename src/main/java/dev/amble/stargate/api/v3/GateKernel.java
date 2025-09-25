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
        this.initBehavior(state);
    }

    @Override
    public <T extends GateState<T>> T removeState(@NotNull GateState.Type<T> type) {
        T result = super.removeState(type);

        if (result != null)
            this.removeBehavior(result);

        return result;
    }

    private final <T extends GateState<T>> void tickBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.tick(this, t));
    }

    private final <T extends GateState<T>> void initBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.init(this, t));
    }

    private final <T extends GateState<T>> void removeBehavior(GateState<T> state) {
        runEvent(state, (behavior, t) -> behavior.finish(this, t));
    }

    private final <T extends GateState<T>> void runEvent(GateState<T> state, BiConsumer<GateBehavior<T>, T> consumer) {
        for (GateBehavior<T> behavior : BehaviorRegistry.get(state.type())) {
            consumer.accept(behavior, state.unbox());
        }
    }
}
