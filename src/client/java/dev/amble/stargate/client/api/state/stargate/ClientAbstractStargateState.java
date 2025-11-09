package dev.amble.stargate.client.api.state.stargate;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.Identifier;

public abstract class ClientAbstractStargateState<T extends BaseStargateModel> implements TState<ClientAbstractStargateState<?>> {

    public static final Type<ClientAbstractStargateState<?>> state = new Type<>(StargateMod.id("meta/client"));

    public int glyphColor = 0x5c5c73;

    public int portalSize = 32;
    public float portalYOffset = -0.9f;
    public Identifier portalType = StargateMod.id("textures/portal/normal.png");

    public int age;

    public final Identifier texture;
    public final Identifier emission;

    private T model;

    public ClientAbstractStargateState(Identifier id) {
        this.texture = id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + ".png");
        this.emission = id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + "_emission.png");

        this.model = this.createModel();
    }

    protected abstract T createModel();

    public T model() {
        return model == null ? model = createModel() : model;
    }

    @Override
    public Type<ClientAbstractStargateState<?>> type() {
        return state;
    }
}
