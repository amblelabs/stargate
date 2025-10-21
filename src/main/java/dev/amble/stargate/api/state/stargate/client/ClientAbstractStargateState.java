package dev.amble.stargate.api.state.stargate.client;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Stargate;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.Identifier;

public abstract class ClientAbstractStargateState implements TState<ClientAbstractStargateState> {

    public static final Type<ClientAbstractStargateState> state = new Type<>(StargateMod.id("meta/client"));

    public int glyphColor = 0x5c5c73;

    public int portalSize = 32;
    public float portalYOffset = -0.9f;
    public Identifier portalType = StargateMod.id("textures/portal/normal.png");

    public int age;

    public final Identifier texture;
    public final Identifier emission;

    public ClientAbstractStargateState(Identifier id) {
        this.texture = id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + ".png");
        this.emission = id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + "_emission.png");
    }

    @Override
    public Type<ClientAbstractStargateState> type() {
        return state;
    }
}
