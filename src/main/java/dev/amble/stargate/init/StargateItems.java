package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.core.fluid.StargateFluids;
import dev.amble.stargate.core.item.DialerItem;
import dev.amble.stargate.core.item.EmptyContainerItem;
import net.minecraft.fluid.Fluids;
import dev.amble.stargate.item.DialerItem;
import net.minecraft.item.Item;

public class StargateItems extends ItemContainer {

	@AutomaticModel
	@NoEnglish
	public static final Item ADDRESS_CARTOUCHE = new DialerItem(new AItemSettings().maxCount(1).group(StargateItemGroups.MAIN));

	//Naquadah
	@AutomaticModel
	@NoEnglish
	public static final Item RAW_NAQUADAH = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

	@AutomaticModel
	@NoEnglish
	public static final Item NAQUADAH_INGOT = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

	@AutomaticModel
	@NoEnglish
	public static final Item NAQUADAH_NUGGET = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

	@AutomaticModel
	@NoEnglish
	public static final Item EMPTY_CONTAINER = new EmptyContainerItem(Fluids.EMPTY, (new AItemSettings().maxCount(16).group(StargateItemGroups.MAIN)));



	// Iris
	@AutomaticModel
	@NoEnglish
	public static final Item IRIS_BLADE = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

	@AutomaticModel
	@NoEnglish
	public static final Item IRIS_FRAME = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

	@AutomaticModel
	@NoEnglish
	public static final Item IRIS = new Item(new AItemSettings().group(StargateItemGroups.MAIN));

}
