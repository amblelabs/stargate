package dev.amble.stargate.core;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.container.impl.BlockContainer;
import dev.amble.lib.container.impl.NoBlockItem;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.datagen.util.PickaxeMineable;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.core.block.DHDBlock;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.StargateRingBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;

public class StargateBlocks extends BlockContainer {
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@AutomaticModel(justItem = true)
	@NoEnglish
	public static final Block STARGATE = new StargateBlock(ABlockSettings.create().itemSettings(new AItemSettings()
					.group(StargateItemGroups.MAIN)).nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(5.5F, 10.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));
	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@NoEnglish
	@AutomaticModel(justItem = true)
	public static final Block DHD = new DHDBlock(ABlockSettings.create().itemSettings(new AItemSettings()
					.group(StargateItemGroups.MAIN)).nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));
	@NoBlockItem
	public static final Block RING = new StargateRingBlock(ABlockSettings.create().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

	@Override
	public Item.Settings createBlockItemSettings(Block block) {
		return new AItemSettings().group(StargateItemGroups.MAIN);
	}

	static {
		ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entry) -> {
		});
	}
}
