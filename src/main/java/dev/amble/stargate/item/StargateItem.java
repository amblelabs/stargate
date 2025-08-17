package dev.amble.stargate.item;

import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.init.StargateBlocks;
import net.minecraft.item.BlockItem;

public class StargateItem extends BlockItem {

    public final GateKernelRegistry.KernelCreator creator;

    public StargateItem(Settings settings, GateKernelRegistry.KernelCreator creator) {
        super(StargateBlocks.STARGATE, settings);
        this.creator = creator;
    }

    public GateKernelRegistry.KernelCreator getCreator() {
        return this.creator;
    }
}
