package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.common.lib.amblekit.StargateEcs;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FabricStargateServerInit implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        StargateEcs.registerAll();
    }
}
