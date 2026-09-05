package dev.amblelabs.stargate.common.lib;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.items.DialerItem;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.items.StargateBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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

    // TODO: make it put gates *dynamically* into the tab
    private static final String[] BUILT_IN_GATES = new String[] {
            "destiny", "milky_way", "pegasus"
    };

    static {
        for (String path : BUILT_IN_GATES) {
            ResourceLocation prototypeId = StargateAPI.modLoc(path);
            make("stargate/" + path, new StargateBlockItem(prototypeId, new Item.Properties()));
        }

        for (IrisItem.Type irisType : IrisItem.Type.ALL) {
            make(irisType.loc().withPrefix("iris/"), new IrisItem(irisType, props()));
        }
    }

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

    public static final Item ADDRESS_CARTOUCHE = make("address_cartouche",
            new DialerItem(props().component(StargateComponents.STARGATE, null).rarity(Rarity.EPIC)));

    public static final ResourceKey<JukeboxSong> STARGATE_THEME_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG,
                    StargateAPI.modLoc("stargate_theme"));

    public static final Item MUSIC_DISC_THEME = make(
            "music_disc/theme",
            new Item(new Item.Properties()
                    .jukeboxPlayable(STARGATE_THEME_SONG)
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
            )
    );

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

    @SuppressWarnings("UnusedReturnValue")
    private static <T extends Item> T make(ResourceLocation id, T item) {
        return make(id, item, StargateCreativeTabs.STARGATE);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> T make(String id, T item, @Nullable CreativeModeTab tab) {
        return make(modLoc(id), item, tab);
    }

    private static <T extends Item> T make(String id, T item) {
        return make(id, item, StargateCreativeTabs.STARGATE);
    }

    private static Supplier<ItemStack> addToTab(Supplier<ItemStack> stack, CreativeModeTab tab) {
        var memoized = Suppliers.memoize(stack::get);
        ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new TabEntry.StackEntry(memoized));
        return memoized;
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