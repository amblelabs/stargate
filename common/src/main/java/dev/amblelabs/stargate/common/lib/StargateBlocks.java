package dev.amblelabs.stargate.common.lib;

import com.mojang.datafixers.util.Pair;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.ToasterBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class StargateBlocks {

    public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
        for (var e : BLOCKS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerBlockItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : BLOCK_ITEMS.entrySet()) {
            r.accept(new BlockItem(e.getValue().getFirst(), e.getValue().getSecond()), e.getKey());
        }
    }

    public static void registerBlockCreativeTab(Consumer<Block> r, CreativeModeTab tab) {
        List<Block> blocks = BLOCK_TABS.get(tab);
        if (blocks == null || blocks.isEmpty()) return;

        for (var block : blocks) {
            r.accept(block);
        }
    }

    private static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, Pair<Block, Item.Properties>> BLOCK_ITEMS = new LinkedHashMap<>();
    private static final Map<CreativeModeTab, List<Block>> BLOCK_TABS = new LinkedHashMap<>();

    private static BlockBehaviour.Properties papery(MapColor color) {
        return BlockBehaviour.Properties
            .of()
            .mapColor(color)
            .sound(SoundType.GRASS)
            .instabreak()
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties woodyHard(MapColor color) {
        return BlockBehaviour.Properties
            .ofFullCopy(Blocks.OAK_LOG)
            .mapColor(color)
            .sound(SoundType.WOOD)
            .strength(3f, 4f);
    }

    private static BlockBehaviour.Properties woody(MapColor color) {
        return BlockBehaviour.Properties
            .ofFullCopy(Blocks.OAK_LOG)
            .mapColor(color)
            .sound(SoundType.WOOD)
            .strength(2f);
    }

    private static BlockBehaviour.Properties leaves(MapColor color) {
        return BlockBehaviour.Properties
            .ofFullCopy(Blocks.OAK_LEAVES)
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((bs, level, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT)
            .isSuffocating(StargateBlocks::never)
            .isViewBlocking(StargateBlocks::never);
    }

    public static final StargateBlock STARGATE = blockItem("stargate", new StargateBlock(StargateBlock.defaultProps()));

    public static final ToasterBlock TOASTER = blockItem("toaster", new ToasterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final Block NAQUADAH_BLOCK = blockItem("naquadah", new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

    @SuppressWarnings("SameReturnValue") // intended
    private static boolean never(Object... args) {
        return false;
    }

    private static <T extends Block> T blockNoItem(String name, T block) {
        var old = BLOCKS.put(modLoc(name), block);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return block;
    }

    private static <T extends Block> T blockItem(String name, T block) {
        return blockItem(name, block, StargateItems.props(), StargateCreativeTabs.STARGATE);
    }

    private static <T extends Block> T blockItem(String name, T block, @Nullable CreativeModeTab tab) {
        return blockItem(name, block, StargateItems.props(), tab);
    }

    private static <T extends Block> T blockItem(String name, T block, Item.Properties props) {
        return blockItem(name, block, props, StargateCreativeTabs.STARGATE);
    }

    private static <T extends Block> T blockItem(String name, T block, Item.Properties props, @Nullable CreativeModeTab tab) {
        blockNoItem(name, block);

        var old = BLOCK_ITEMS.put(modLoc(name), new Pair<>(block, props));
        if (old != null) throw new IllegalArgumentException("Duplicate id " + name);

        if (tab != null) {
            BLOCK_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(block);
        }

        return block;
    }
}

