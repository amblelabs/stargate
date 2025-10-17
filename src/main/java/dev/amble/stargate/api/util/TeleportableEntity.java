package dev.amble.stargate.api.util;

public interface TeleportableEntity {
    void stargate$setTicks(int ticks);

    default void stargate$updateTicks(int max) {
        if (stargate$ticks() != 0)
            stargate$setTicks(max);
    }

    default int stargate$updateAndGetTicks(int max) {
        stargate$updateTicks(max);
        return stargate$ticks();
    }

    int stargate$ticks();

    default void stargate$tickTicks() {
        int ticks = this.stargate$ticks();

        if (ticks > 0) {
            this.stargate$setTicks(ticks - 1);
        }
    }
}
