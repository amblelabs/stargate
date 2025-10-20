package dev.amble.stargate.api.state.stargate;

import dev.amble.stargate.StargateMod;
import net.minecraft.util.Identifier;

public class PegasusState extends GateIdentityState implements C8Gates {

    public static final Identifier ID = StargateMod.id("pegasus");

    public PegasusState() {
        super(ID);
    }
}
