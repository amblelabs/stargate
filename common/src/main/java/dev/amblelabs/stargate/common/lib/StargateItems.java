package dev.amblelabs.stargate.common.lib;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.common.items.IrisItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

// https://github.com/VazkiiMods/Botania/blob/2c4f7fdf9ebf0c0afa1406dfe1322841133d75fa/Common/src/main/java/vazkii/botania/common/item/ModItems.java
@SuppressWarnings("unused")
public class StargateItems {

    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ITEMS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerItemCreativeTab(CreativeModeTab.Output r, CreativeModeTab tab) {
        for (var item : ITEM_TABS.getOrDefault(tab, List.of())) {
            item.register(r);
        }
    }

    private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>(); // preserve insertion order
    private static final Map<CreativeModeTab, List<TabEntry>> ITEM_TABS = new LinkedHashMap<>();

    public static final Item TOAST = make("toast", new Item(props().food(
            new FoodProperties.Builder().nutrition(2)
                    .saturationModifier(0.1f)
                    .alwaysEdible()
                    .build()
    )));

    public static final Item BURNT_TOAST = make("burnt_toast", new Item(props().food(
            new FoodProperties.Builder().nutrition(2)
                    .saturationModifier(0.1f)
                    .alwaysEdible()
                    .build()
    )));

    public static final Item TRINIUM_INGOT = make("trinium_ingot", new Item(props()));
    public static final Item NAQUADAH_INGOT = make("naquadah_ingot", new Item(props()));
    public static final Item NAQUADAH_NUGGET = make("naquadah_nugget", new Item(props()));
    public static final Item RAW_NAQUADAH = make("raw_naquadah", new Item(props()));

    public static final Item CRYSTAL_INGOT = make("crystal_ingot", new Item(props()));

    public static final Item TRINIUM_IRIS = make("trinium_iris", new IrisItem(IrisItem.Type.TRINIUM, props()));
    public static final Item NAQUADAH_IRIS = make("naquadah_iris", new IrisItem(IrisItem.Type.NAQUADAH, props()));
    public static final Item NETHERITE_IRIS = make("netherite_iris", new IrisItem(IrisItem.Type.NETHERITE, props()));
    public static final Item DIAMOND_IRIS = make("diamond_iris", new IrisItem(IrisItem.Type.DIAMOND, props()));
    public static final Item GOLD_IRIS = make("gold_iris", new IrisItem(IrisItem.Type.GOLD, props()));
    public static final Item IRON_IRIS = make("iron_iris", new IrisItem(IrisItem.Type.IRON, props()));

    public static Item.Properties props() {
        return new Item.Properties();
    }

    public static Item.Properties unstackable() {
        return props().stacksTo(1);
    }

    private static <T extends Item> T make(ResourceLocation id, T item, @Nullable CreativeModeTab tab) {
        var old = ITEMS.put(id, item);
        if (old != null) throw new IllegalArgumentException("Duplicate id " + id);

        if (tab != null) ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>())
                .add(new TabEntry.ItemEntry(item));

        return item;
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> T make(String id, T item, @Nullable CreativeModeTab tab) {
        return make(modLoc(id), item, tab);
    }

    private static <T extends Item> T make(String id, T item) {
        return make(id, item, StargateCreativeTabs.STARGATE);
    }

    private static Supplier<ItemStack> addToTab(Supplier<ItemStack> stack, CreativeModeTab tab) {
        var memoised = Suppliers.memoize(stack::get);
        ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new TabEntry.StackEntry(memoised));
        return memoised;
    }

    private static abstract class TabEntry {
        abstract void register(CreativeModeTab.Output r);

        static class ItemEntry extends TabEntry {
            private final Item item;

            ItemEntry(Item item) {
                this.item = item;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(item);
            }
        }

        static class StackEntry extends TabEntry {
            private final Supplier<ItemStack> stack;

            StackEntry(Supplier<ItemStack> stack) {
                this.stack = stack;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(stack.get());
            }
        }
    }
}