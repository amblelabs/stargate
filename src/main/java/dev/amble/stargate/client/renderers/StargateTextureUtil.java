package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.kernels.impl.DestinyGateKernel;
import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.kernels.impl.PegasusGateKernel;
import dev.amble.stargate.api.v2.Stargate;
import net.minecraft.util.Identifier;

// TODO make them datapackable because I hate this :( - Loqor
public class StargateTextureUtil {
    public static final Identifier MILKY_WAY = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way.png");
    public static final Identifier MILKY_WAY_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way_emission.png");
    public static final Identifier PEGASUS = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/pegasus/pegasus.png");
    public static final Identifier PEGASUS_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/pegasus/pegasus_emission.png");
    public static final Identifier DESTINY = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/destiny/destiny.png");
    public static final Identifier DESTINY_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/destiny/destiny_emission.png");
    public static final Identifier ORLIN = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/orlin/orlin.png");
    public static final Identifier ORLIN_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/orlin/orlin_emission.png");

    public static Identifier getTextureForGate(Stargate gate) {
        var impl = gate.kernel();
        if (impl instanceof PegasusGateKernel) return PEGASUS;
        if (impl instanceof DestinyGateKernel) return DESTINY;
        if (impl instanceof OrlinGateKernel) return ORLIN;
        return MILKY_WAY;
    }

    public static Identifier getEmissionForGate(Stargate gate) {
        var impl = gate.kernel();
        if (impl instanceof PegasusGateKernel) return PEGASUS_EMISSION;
        if (impl instanceof DestinyGateKernel) return DESTINY_EMISSION;
        if (impl instanceof OrlinGateKernel) return ORLIN_EMISSION;
        return MILKY_WAY_EMISSION;
    }
}