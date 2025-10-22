package dev.amble.stargate.item;

import dev.amble.stargate.api.GateKernelRegistry;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.init.StargateBlocks;
import net.minecraft.item.BlockItem;

public class StargateItem extends BlockItem {

    public final GateKernelRegistry.Entry creator;

    public StargateItem(Settings settings, GateKernelRegistry.Entry creator) {
        this(settings, StargateBlocks.GENERIC_GATE, creator);
    }

    private StargateItem(Settings settings, StargateBlock block, GateKernelRegistry.Entry entry) {
        super(block, settings);
        this.creator = entry;
    }

    @Override
    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    public GateKernelRegistry.Entry getCreator() {
        return this.creator;
    }
}
