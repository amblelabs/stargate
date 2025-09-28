package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.fluid.StargateFluids;
import dev.amble.stargate.item.DialerItem;
import dev.amble.stargate.item.EmptyContainerItem;
import dev.amble.stargate.item.StargateItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.jetbrains.annotations.Nullable;

public class StargateItems extends ItemContainer {

	@AutomaticModel
	@NoEnglish
	public static final Item ADDRESS_CARTOUCHE = new DialerItem(new AItemSettings().maxCount(1));

	// Stargates
	public static final Item MILKY_WAY_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.MILKY_WAY);
	public static final Item ORLIN_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.ORLIN);
	public static final Item DESTINY_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.DESTINY);
	public static final Item PEGASUS_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.PEGASUS);

	//Naquadah
	@AutomaticModel
	@NoEnglish
	public static final Item RAW_NAQUADAH = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item NAQUADAH_INGOT = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item NAQUADAH_NUGGET = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item EMPTY_CONTAINER = new EmptyContainerItem(Fluids.EMPTY, (new AItemSettings().maxCount(16)));

	@AutomaticModel
	@NoEnglish
	public static final Item COPPER_COIL = new Item(new AItemSettings());

	// Iris
	@AutomaticModel
	@NoEnglish
	public static final Item IRIS_BLADE = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item IRIS_FRAME = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item IRIS = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item CONTROL_CRYSTAL_YELLOW = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item CONTROL_CRYSTAL_BLUE = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item CONTROL_CRYSTAL_RED = new Item(new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item CONTROL_CRYSTAL_MASTER = new Item(new AItemSettings());

	@NoEnglish
	public static final Item LIQUID_NAQUADAH = new EmptyContainerItem(StargateFluids.STILL_LIQUID_NAQUADAH, new FabricItemSettings().recipeRemainder(StargateItems.EMPTY_CONTAINER).maxCount(1));

    @AutomaticModel
    @NoEnglish
    public static final Item TOAST = new Item(new AItemSettings().group(StargateItemGroups.MAIN).food(StargateFoodComponenets.TOAST));

    @AutomaticModel
    @NoEnglish
    public static final Item BURNT_TOAST = new Item(new AItemSettings().group(StargateItemGroups.MAIN).food(StargateFoodComponenets.TOAST));

	@Override
	public @Nullable ItemGroup getDefaultGroup() {
		return StargateItemGroups.MAIN;
	}
}
