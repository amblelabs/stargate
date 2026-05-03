package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.YoureFiredAPI;
import net.neoforged.fml.common.Mod;

@Mod(YoureFiredAPI.MOD_ID)
public final class StargateNeoForge {
    public YoureFiredNeoForge() {
        // Run our common setup.
        YoureFired.init();
    }
}
