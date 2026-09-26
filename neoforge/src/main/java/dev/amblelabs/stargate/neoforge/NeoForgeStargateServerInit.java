package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = StargateAPI.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class NeoForgeStargateServerInit {

    public NeoForgeStargateServerInit() {
        StargateEcs.registerAll();
    }
}
