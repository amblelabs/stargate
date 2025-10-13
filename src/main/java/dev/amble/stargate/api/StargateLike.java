package dev.amble.stargate.api;

import dev.amble.stargate.api.v3.Stargate;
import org.jetbrains.annotations.Nullable;

public interface StargateLike {
    @Nullable Stargate asGate();
}
