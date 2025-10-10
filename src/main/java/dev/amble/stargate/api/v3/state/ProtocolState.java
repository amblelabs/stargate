package dev.amble.stargate.api.v3.state;

import dev.drtheo.yaar.state.TState;

public interface ProtocolState extends TState<ProtocolState> {

    default int maxChevrons() {
        return 9;
    }
}
