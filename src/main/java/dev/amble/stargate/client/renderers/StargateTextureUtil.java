package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.v3.Stargate;
import net.minecraft.util.Identifier;

// TODO make them datapackable because I hate this :( - Loqor
public class StargateTextureUtil {

    public static Identifier getTextureForGate(Stargate gate) {
        return gate.id().withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + ".png");
    }

    public static Identifier getEmissionForGate(Stargate gate) {
        return gate.id().withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + "_emission.png");
    }
}