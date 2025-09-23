package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemGroupContainer;
import dev.amble.lib.itemgroup.AItemGroup;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.fluid.StargateFluids;
import net.minecraft.item.ItemStack;



public class StargateItemGroups implements ItemGroupContainer {
    public static final AItemGroup MAIN = AItemGroup.builder(StargateMod.id("item_group"))
            .icon(() -> new ItemStack(StargateBlocks.DHD))
            .entries((context, entries) -> {
                entries.add(StargateFluids.LIQUID_NAQUADAH);
            })
            .build();
}
