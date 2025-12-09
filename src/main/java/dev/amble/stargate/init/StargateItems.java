package dev.amble.stargate.init;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.api.GateKernelRegistry;
import dev.amble.stargate.item.*;
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
	@NoEnglish public static final StargateItem MILKY_WAY_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.MILKY_WAY);
	@NoEnglish public static final StargateItem ORLIN_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.ORLIN);
	@NoEnglish public static final StargateItem DESTINY_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.DESTINY);
	@NoEnglish public static final StargateItem PEGASUS_STARGATE = new StargateItem(new AItemSettings(), GateKernelRegistry.PEGASUS);

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
	public static final Item NAQUADAH_BOAT = new NaquadahBoatItem(false, new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item EMPTY_CONTAINER = new EmptyContainerItem(Fluids.EMPTY, new AItemSettings().maxCount(16));

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
	public static final Item IRON_IRIS = new IrisItem(StargateIrisTiers.IRON, new AItemSettings());

	@AutomaticModel
	@NoEnglish
	public static final Item GOLD_IRIS = new IrisItem(StargateIrisTiers.GOLD, new AItemSettings());

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
	public static final Item LIQUID_NAQUADAH = new EmptyContainerItem(StargateFluids.LIQUID_NAQUADAH_STILL, new FabricItemSettings().recipeRemainder(StargateItems.EMPTY_CONTAINER).maxCount(1));

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
