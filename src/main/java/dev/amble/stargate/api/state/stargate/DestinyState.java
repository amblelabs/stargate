package dev.amble.stargate.api.state.stargate;

import dev.amble.stargate.StargateMod;
import net.minecraft.util.Identifier;

public class DestinyState extends GateIdentityState {

    public static final Identifier ID = StargateMod.id("destiny");

    public DestinyState() {
        super(ID);
    }
}
