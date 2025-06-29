package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.item.DialerItem;
import net.minecraft.item.Item;

public class StargateItems extends ItemContainer {

	@NoEnglish
	public static final Item ADDRESS_CARTOUCHE = new DialerItem(new AItemSettings().maxCount(1).group(StargateItemGroups.MAIN));
}
