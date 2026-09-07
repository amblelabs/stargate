package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.blocks.DHDBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateRingBlock;
import dev.amblelabs.stargate.common.blocks.ToasterBlock;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static dev.amblelabs.stargate.common.lib.StargateItems.Lazy;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class StargateBlocks {

    private static final XplatRegister<Block> REGISTER_BLOCKS = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.BLOCK);
    private static final XplatRegister<Item> REGISTER_BLOCK_ITEMS = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.ITEM);
    private static final Map<ResourceKey<CreativeModeTab>, List<Supplier<Block>>> BLOCK_TABS = new LinkedHashMap<>();

    public static void register() {
        REGISTER_BLOCKS.registerAll();
        REGISTER_BLOCK_ITEMS.registerAll();
    }

    public static Collection<ResourceKey<CreativeModeTab>> getCreativeTabs() {
        return BLOCK_TABS.keySet();
    }

    public static void registerCreativeTabBlocks(ResourceKey<CreativeModeTab> tabKey, CreativeModeTab.Output r) {
        for (Supplier<Block> blockSupplier : BLOCK_TABS.getOrDefault(tabKey, List.of())) {
            r.accept(blockSupplier.get());
        }
    }

    // needs to have a block item
    public static final Lazy<StargateBlock> STARGATE = blockItem("stargate",
            () -> new StargateBlock(StargateBlock.defaultProps()), (ResourceKey<CreativeModeTab>) null);

    public static final Lazy<Block> RING = blockNoItem("ring", () -> new StargateRingBlock(Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion()));

    public static final Lazy<ToasterBlock> TOASTER = blockItem("toaster", () -> new ToasterBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final Lazy<Block> NAQUADAH_BLOCK = blockItem("naquadah_block", () -> new Block(Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));

    public static final Lazy<DHDBlock> DHD_BLOCK = blockItem("dhd", () -> new DHDBlock(DHDBlock.defaultProps()));

    public static final Lazy<Block> NAQUADAH_ORE = blockItem("naquadah_ore", () -> new Block(Properties.ofFullCopy(Blocks.DEEPSLATE_COAL_ORE)));

    public static final Lazy<Block> RAW_NAQUADAH_BLOCK = blockItem("raw_naquadah_block", () -> new Block(Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK)));

    public static final Lazy<Block> SANDSTONE_BRICKS = blockItem("sandstone_bricks", () -> new Block(Properties.ofFullCopy(Blocks.SANDSTONE)));

    public static final Lazy<Block> SANDSTONE_BRICK_STAIRS = blockItem("sandstone_brick_stairs", () -> new StairBlock(SANDSTONE_BRICKS.get().defaultBlockState(), Properties.ofFullCopy(Blocks.SANDSTONE_STAIRS)) {});

    public static final Lazy<Block> SANDSTONE_BRICK_SLAB = blockItem("sandstone_brick_slab", () -> new SlabBlock(Properties.ofFullCopy(Blocks.SANDSTONE_SLAB)));

    public static final Lazy<Block> SANDSTONE_BRICK_WALL = blockItem("sandstone_brick_wall", () -> new WallBlock(Properties.ofFullCopy(Blocks.SANDSTONE_WALL)));

    public static final Lazy<Block> DRY_GRASS = blockItem("dry_grass", () -> new DeadBushBlock(Properties.ofFullCopy(Blocks.DEAD_BUSH)) {});

    public static final Lazy<Block> DRY_BUSH = blockItem("dry_bush", () -> new DeadBushBlock(Properties.ofFullCopy(Blocks.DEAD_BUSH)) {});

    @SuppressWarnings("SameReturnValue") // intended
    private static boolean never(Object... args) {
        return false;
    }

    private static <T extends Block> Lazy<T> blockNoItem(String name, Supplier<T> supplier) {
        supplier = REGISTER_BLOCKS.register(name, supplier);
        return supplier::get;
    }

    private static <T extends Block> Lazy<T> blockItem(String name, Supplier<T> block) {
        return blockItem(name, block, StargateCreativeTabs.STARGATE_KEY);
    }

    private static <T extends Block> Lazy<T> blockItem(String name, Supplier<T> block, @Nullable ResourceKey<CreativeModeTab> tab) {
        return blockItem(name, block, new Item.Properties(), tab);
    }

    private static <T extends Block> Lazy<T> blockItem(String name, Supplier<T> block, Item.Properties props) {
        return blockItem(name, block, props, StargateCreativeTabs.STARGATE_KEY);
    }

    private static <T extends Block> Lazy<T> blockItem(String name, Supplier<T> block, Item.Properties props, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        Lazy<T> finalSupplier = blockNoItem(name, block);
        REGISTER_BLOCK_ITEMS.register(name, () -> new BlockItem(finalSupplier.get(), props));

        if (tabKey != null)
            BLOCK_TABS.computeIfAbsent(tabKey, t -> new ArrayList<>())
                    .add(finalSupplier::get);

        return finalSupplier;
    }
}

