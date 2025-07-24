package dev.amble.stargate.api;

public interface TeleportableEntity {
    void stargate$setStatus(State inGate);

    default void stargate$updateStatus() {
        if (stargate$status() == State.PENDING)
            stargate$setStatus(State.IN_GATE);
    }

    default State stargate$updateAndGetStatus() {
        stargate$updateStatus();
        return stargate$status();
    }

    State stargate$status();

    enum State {
        IN_GATE(true) {
            @Override
            public State next() {
                return PENDING;
            }
        },
        PENDING(true) {
            @Override
            public State next() {
                return OUTSIDE;
            }
        },
        OUTSIDE(false) {
            @Override
            public State next() {
                return OUTSIDE;
            }
        };

        public static final State[] VALS = State.values();

        private final boolean value;

        State(boolean value) {
            this.value = value;
        }

        public boolean isInGate() {
            return value;
        }

        public abstract State next();
    }
}
