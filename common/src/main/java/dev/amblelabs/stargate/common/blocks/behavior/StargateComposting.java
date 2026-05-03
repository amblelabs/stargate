package dev.amblelabs.stargate.common.blocks.behavior;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public final class StargateComposting {

    @SuppressWarnings("EmptyMethod")
    public static void setup() {

    }

    @SuppressWarnings("unused")
    private static void compost(ItemLike itemLike, float chance) {
        Item item = itemLike.asItem();

        if (item != Items.AIR) {
            ComposterBlock.COMPOSTABLES.put(item, chance);
        }
    }
}