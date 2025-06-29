package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemGroupContainer;
import dev.amble.lib.itemgroup.AItemGroup;
import dev.amble.stargate.StargateMod;
import net.minecraft.item.ItemStack;

public class StargateItemGroups implements ItemGroupContainer {
    public static final AItemGroup MAIN = AItemGroup.builder(StargateMod.id("item_group"))
            .icon(() -> new ItemStack(StargateBlocks.DHD)).build();
}
