package dev.amble.stargate.api.v3;

import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v2.Stargate;
import org.jetbrains.annotations.NotNull;

public class DefaultGateStates extends BasicGateStates<DefaultGateStates> {

    public static final DefaultGateStates INSTANCE = new DefaultGateStates();

    @Override
    public GateState<DefaultGateStates> getDefault() {
        return new Closed();
    }

    @Override
    protected void init(StatePopulator<DefaultGateStates> populator) {
        populator.put(GateState.Type.CLOSED, Closed::fromNbt);
        populator.put(GateState.Type.PRE_OPEN, PreOpen::fromNbt);
        populator.put(GateState.Type.OPEN, Open::fromNbt);
    }

    static class Closed extends GateStates.Closed<DefaultGateStates> {

    }

    static class PreOpen extends GateStates.PreOpen<DefaultGateStates> {

        public PreOpen(String address, boolean caller) {
            super(address, caller);
        }
    }

    static class Open extends GateStates.Open<DefaultGateStates> {

        public Open(@NotNull StargateRef target, boolean caller) {
            super(target, caller);
        }

        public Open(Stargate stargate, boolean caller) {
            super(stargate, caller);
        }
    }
}
