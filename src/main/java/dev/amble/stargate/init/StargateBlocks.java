package dev.amble.stargate.init;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.container.impl.BlockContainer;
import dev.amble.lib.container.impl.NoBlockItem;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoBlockDrop;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.datagen.util.PickaxeMineable;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.block.*;
import dev.amble.stargate.block.stargates.OrlinGateBlock;
import dev.amble.stargate.fluid.LiquidNaquadahFluidBlock;
import dev.amble.stargate.fluid.StargateFluids;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

@SuppressWarnings("unused")
public class StargateBlocks extends BlockContainer {

	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@NoEnglish
	@NoBlockItem
	public static final StargateBlock GENERIC_GATE = new StargateBlock(ABlockSettings.create()
			.nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(5.5F, 10.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@NoEnglish
	@NoBlockItem
	public static final StargateBlock ORLIN_GATE = new OrlinGateBlock(new ABlockSettings()
			.nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(5.5F, 10.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

	@PickaxeMineable(tool = PickaxeMineable.Tool.IRON)
	@NoEnglish
	@AutomaticModel(justItem = true)
	public static final Block DHD = new DHDBlock(new ABlockSettings().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));
	
	@NoBlockItem
	public static final Block RING = new StargateRingBlock(new ABlockSettings().nonOpaque().requiresTool().instrument(Instrument.BASEDRUM).strength(0.5F, 6.0F)
			.pistonBehavior(PistonBehavior.IGNORE).luminance(light -> 3));

	@AutomaticModel
	@NoEnglish
	public static final Block PEANUT = new Block(new ABlockSettings().itemSettings(new AItemSettings()));

	@NoEnglish
	@NoBlockDrop
	public static final Block LIQUID_NAQUADAH_BLOCK = new LiquidNaquadahFluidBlock(StargateFluids.LIQUID_NAQUADAH_STILL,
			ABlockSettings.create().itemSettings(new AItemSettings()).mapColor(MapColor.EMERALD_GREEN).replaceable()
					.noCollision().strength(100.0F).pistonBehavior(PistonBehavior.DESTROY).dropsNothing().liquid()
					.sounds(BlockSoundGroup.INTENTIONALLY_EMPTY));

	@AutomaticModel
	@NoEnglish
	@NoBlockDrop
	@PickaxeMineable(tool = PickaxeMineable.Tool.DIAMOND)
	public static final Block NAQUADAH_ORE = new Block(ABlockSettings.create().nonOpaque().requiresTool()
			.strength(10.0F, 6.0F).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.ANCIENT_DEBRIS));

	@AutomaticModel
	@NoEnglish
	@PickaxeMineable(tool = PickaxeMineable.Tool.DIAMOND)
	public static final Block NAQUADAH_BLOCK = new Block(ABlockSettings.create().nonOpaque().requiresTool()
			.strength(25.0F, 9.0F).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.NETHERITE));

	@AutomaticModel
	@NoEnglish
	@PickaxeMineable(tool = PickaxeMineable.Tool.DIAMOND)
	public static final Block RAW_NAQUADAH_BLOCK = new Block(ABlockSettings.create().nonOpaque().requiresTool()
			.strength(15.0F, 7.5F).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.NETHERITE));

    @NoEnglish
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block TOASTER = new ToasterBlock(ABlockSettings.create().nonOpaque().requiresTool()
            .strength(10.0F, 1.0F).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.METAL));

    @NoEnglish
    @PickaxeMineable(tool = PickaxeMineable.Tool.STONE)
    public static final Block COMPUTER = new DialingComputerBlock(ABlockSettings.create().nonOpaque()
            .strength(10.0F, 1.0F).pistonBehavior(PistonBehavior.NORMAL).sounds(BlockSoundGroup.METAL));

	@Override
	public Item.Settings createBlockItemSettings(Block block) {
		return new AItemSettings().group(StargateItemGroups.MAIN);
	}
}
