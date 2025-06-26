package dev.amble.stargate.core;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.container.impl.ItemGroupContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.lib.itemgroup.AItemGroup;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.core.item.DialerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class StargateItems extends ItemContainer {

	@NoEnglish
	public static final Item ADDRESS_CARTOUCHE = new DialerItem(new AItemSettings().maxCount(1).group(StargateItemGroups.MAIN));
}
