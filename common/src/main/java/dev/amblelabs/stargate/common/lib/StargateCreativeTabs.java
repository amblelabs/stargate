package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateCreativeTabs {
    public static void registerCreativeTabs(BiConsumer<CreativeModeTab, ResourceLocation> r) {
        for (var e : TABS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, CreativeModeTab> TABS = new LinkedHashMap<>();

    public static final CreativeModeTab AIT = register("main", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 7)
            .icon(() -> new ItemStack(Items.FIRE_CHARGE)));

    @SuppressWarnings("SameParameterValue")
    private static CreativeModeTab register(String name, CreativeModeTab.Builder tabBuilder) {
        var tab = tabBuilder.title(Component.translatable("itemGroup." + StargateAPI.MOD_ID + "." + name)).build();
        var old = TABS.put(modLoc(name), tab);

        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }

        return tab;
    }
}