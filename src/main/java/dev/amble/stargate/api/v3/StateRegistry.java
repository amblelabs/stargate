package dev.amble.stargate.api.v3;

import java.util.ArrayList;
import java.util.List;

public class StateRegistry {

    private static final List<GateState.Type<?>> states = new ArrayList<>();

    public static void register(GateState.Type<?> state) {
        state.key().index = states.size();
        states.add(state);
    }

    public static void register(GateState.Group group) {
        for (GateState.Type<?> type : group.types()) {
            register(type);
        }
    }
}
