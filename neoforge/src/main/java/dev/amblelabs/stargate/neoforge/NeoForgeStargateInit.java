package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.blocks.behavior.StargateComposting;
import dev.amblelabs.stargate.common.blocks.behavior.StargateStrippable;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.neoforged.fml.common.Mod;

@Mod(StargateAPI.MOD_ID)
public final class NeoForgeStargateInit {

    public NeoForgeStargateInit() {
        FabricStargateConfig.setup();

        FabricPacketHandler.initPackets();
        FabricPacketHandler.init();

        this.initListeners();

        StargateInterop.earlyInit();
        this.initRegistries();

        StargateComposting.setup();
        StargateStrippable.init();

        StargateInterop.init();
        StargateEcs.init();
    }
}
