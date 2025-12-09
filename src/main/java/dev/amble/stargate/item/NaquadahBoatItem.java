package dev.amble.stargate.item;

import dev.amble.stargate.util.ExBoatType;
import net.minecraft.item.BoatItem;

public class NaquadahBoatItem extends BoatItem {

    public NaquadahBoatItem(boolean chest, Settings settings) {
        super(chest, ExBoatType.NAQUADAH, settings);
    }
}
