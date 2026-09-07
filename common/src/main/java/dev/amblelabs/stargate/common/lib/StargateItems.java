package dev.amblelabs.stargate.common.lib;

import com.google.common.base.Suppliers;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.items.DialerItem;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.items.StargateBlockItem;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

@SuppressWarnings("unused")
public class StargateItems {

    private static final XplatRegister<Item> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.ITEM);
    private static final Map<ResourceKey<CreativeModeTab>, List<TabEntry>> ITEM_TABS = new LinkedHashMap<>();

    public static void register() {
        REGISTER.registerAll();
    }

    public static Collection<ResourceKey<CreativeModeTab>> getCreativeTabs() {
        return ITEM_TABS.keySet();
    }

    public static void registerCreativeTabItems(ResourceKey<CreativeModeTab> tabKey, CreativeModeTab.Output r) {
        for (TabEntry entry : ITEM_TABS.getOrDefault(tabKey, List.of())) {
            entry.register(r);
        }
    }

    // TODO: make it put gates *dynamically* into the tab
    private static final String[] BUILT_IN_GATES = new String[] {
            "destiny", "milky_way", "pegasus"
    };

    static {
        make("stargate/any", () -> new StargateBlockItem(props()));

        for (String path : BUILT_IN_GATES) {
            ResourceLocation prototypeId = StargateAPI.modLoc(path);
            make("stargate/" + path, () -> new StargateBlockItem(prototypeId, props()));
        }
    }

    public static final Lazy<IrisItem> TRINIUM_IRIS = iris(IrisItem.Type.TRINIUM);
    public static final Lazy<IrisItem> NAQUADAH_IRIS = iris(IrisItem.Type.NAQUADAH);
    public static final Lazy<IrisItem> NETHERITE_IRIS = iris(IrisItem.Type.NETHERITE);
    public static final Lazy<IrisItem> DIAMOND_IRIS = iris(IrisItem.Type.DIAMOND);
    public static final Lazy<IrisItem> GOLD_IRIS = iris(IrisItem.Type.GOLD);
    public static final Lazy<IrisItem> IRON_IRIS = iris(IrisItem.Type.IRON);

    public static final Lazy<Item> TOAST = make("toast", props().food(
            new FoodProperties.Builder().nutrition(2)
                    .saturationModifier(0.1f)
                    .alwaysEdible()
                    .build()
    ));

    public static final Lazy<Item> BURNT_TOAST = make("burnt_toast", props().food(
            new FoodProperties.Builder().nutrition(2)
                    .saturationModifier(0.1f)
                    .alwaysEdible()
                    .build()
    ));

    public static final Lazy<Item> TRINIUM_INGOT = make("trinium_ingot");
    public static final Lazy<Item> NAQUADAH_INGOT = make("naquadah_ingot");
    public static final Lazy<Item> NAQUADAH_NUGGET = make("naquadah_nugget");
    public static final Lazy<Item> RAW_NAQUADAH = make("raw_naquadah");

    public static final Lazy<Item> CRYSTAL_INGOT = make("crystal_ingot");

    @SuppressWarnings("DataFlowIssue")
    public static final Lazy<Item> ADDRESS_CARTOUCHE = make("address_cartouche", () ->
            new DialerItem(props().component(StargateComponents.STARGATE.get(), null).rarity(Rarity.EPIC)));

    public static final Lazy<Item> MUSIC_DISC_THEME = make("music_disc/theme",
            props().jukeboxPlayable(StargateJukeboxSongs.THEME_SONG).stacksTo(1).rarity(Rarity.RARE));

    private static Item.Properties props() {
        return new Item.Properties();
    }

    private static Item.Properties unstackable() {
        return props().stacksTo(1);
    }

    private static Lazy<IrisItem> iris(IrisItem.Type type) {
        return make("iris/" + type.toString().toLowerCase(Locale.ROOT), () -> new IrisItem(type, props()));
    }

    private static <T extends Item> Lazy<T> make(ResourceLocation id, Supplier<T> supplier, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        Supplier<T> finalSupplier = REGISTER.register(id, supplier);

        if (tabKey != null)
            ITEM_TABS.computeIfAbsent(tabKey, t -> new ArrayList<>())
                    .add(new TabEntry.ItemEntry(finalSupplier::get));

        return finalSupplier::get;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static <T extends Item> Lazy<T> make(ResourceLocation id, Supplier<T> item) {
        return make(id, item, StargateCreativeTabs.STARGATE_KEY);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> Lazy<T> make(String id, Supplier<T> item, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        return make(modLoc(id), item, tabKey);
    }

    private static <T extends Item> Lazy<T> make(String id, Supplier<T> item) {
        return make(id, item, StargateCreativeTabs.STARGATE_KEY);
    }

    private static Lazy<Item> make(ResourceLocation id, Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        return make(id, () -> new Item(properties), tabKey);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static Lazy<Item> make(ResourceLocation id, Item.Properties properties) {
        return make(id, properties, StargateCreativeTabs.STARGATE_KEY);
    }

    @SuppressWarnings("SameParameterValue")
    private static Lazy<Item> make(String id, Item.Properties properties, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        return make(modLoc(id), properties, tabKey);
    }

    private static Lazy<Item> make(String id, Item.Properties properties) {
        return make(id, properties, StargateCreativeTabs.STARGATE_KEY);
    }

    private static Lazy<Item> make(ResourceLocation id, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        return make(id, props(), tabKey);
    }

    private static Lazy<Item> make(ResourceLocation id) {
        return make(id, props());
    }

    private static Lazy<Item> make(String id, @Nullable ResourceKey<CreativeModeTab> tabKey) {
        return make(modLoc(id), tabKey);
    }

    private static Lazy<Item> make(String id) {
        return make(modLoc(id));
    }

    private static Supplier<ItemStack> addToTab(Supplier<ItemStack> stack, ResourceKey<CreativeModeTab> tab) {
        var memoized = Suppliers.memoize(stack::get);
        ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new TabEntry.StackEntry(memoized));
        return memoized;
    }

    private static abstract class TabEntry {
        abstract void register(CreativeModeTab.Output r);

        static class ItemEntry extends TabEntry {
            private final Lazy<Item> item;

            ItemEntry(Lazy<Item> item) {
                this.item = item;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(item.get());
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

    @FunctionalInterface
    public interface Lazy<T extends ItemLike> extends ItemLike, Supplier<T> {

        @Override
        T get();

        @Override
        default Item asItem() {
            return get().asItem();
        }
    }
}