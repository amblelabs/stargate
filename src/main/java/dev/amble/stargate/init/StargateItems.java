package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItem;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.item.EmptyContainerItem;
import dev.amble.stargate.item.StargateItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.fluid.Fluids;
import dev.amble.stargate.item.DialerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

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

	static {
		List<Item> items = new ArrayList<>();

		for(Identifier id : GateKernelRegistry.get().getIds()) {
			Item item = new StargateItem(new AItemSettings().group(StargateItemGroups.MAIN), GateKernelRegistry.get().get(id));
			Registry.register(Registries.ITEM, id, item);
			items.add(item);
		}

		ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
			for (Item item : items) {
				ItemGroup target = ((AItem) item).a$group();
				if (target == null) {
					target = StargateItemGroups.MAIN;
				}

				if (target == group) {
					entries.add(item);
				}
			}
		});
	}
}
