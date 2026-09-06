package dev.amblelabs.lib.api.ecs;

import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link TStateContainer} that wraps around another container, preventing the original container from being modified.
 */
public class ImmutableTStateContainer extends TStateContainer.Delegate {

    public static final TStateContainer EMPTY = new ImmutableTStateContainer(StargateEcs.StaticStates.createArrayHolder());

    /**
     * @param container the parent container to delegate {@link TStateContainer} calls to.
     */
    public ImmutableTStateContainer(TStateContainer container) {
        super(container);
    }

    @Override
    public boolean addState(TState<?> state) {
        return false;
    }

    @Override
    public void clearStates() { }

    @Override
    public @Nullable <T extends TState<? extends T>> T removeState(TState.Type<? extends T> type) {
        return null;
    }
}
