package dev.amblelabs.stargate.common;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.Locale;

public interface I18n {
    Component LOGO_TOOLTIP = modText("text", "logo");

    interface Items {
        Component GENERIC_ITEM_SHIFT_TOOLTIP = item("generic.tooltip.shift");
        Component DIALER_FAIL = item(StargateItems.ADDRESS_CARTOUCHE, "fail");
        Component DIALER_TOOLTIP = item(StargateItems.ADDRESS_CARTOUCHE, "tooltip");
        Component DIALER_TOOLTIP_HEADER = item(StargateItems.ADDRESS_CARTOUCHE, "tooltip.header");

        static MutableComponent dialerDetails(Stargate stargate) {
            return item(StargateItems.ADDRESS_CARTOUCHE, "tooltip.details", stargate.getId());
        }
    }

    interface Commands {
        interface Arguments {
            Component NOT_FOUND = modText("argument", "notfound");
        }

        static MutableComponent dataModified(BlockPos pos) {
            return dataModified(pos.getX(), pos.getY(), pos.getZ());
        }

        static MutableComponent dataModified(int x, int y, int z) {
            return command("data", "modified", x, y, z);
        }

        static MutableComponent dataQuery(BlockPos pos, Component component) {
            return dataQuery(pos.getX(), pos.getY(), pos.getZ(), component);
        }

        static MutableComponent dataQuery(int x, int y, int z, Component component) {
            return command("data", "query", x, y, z, component);
        }

        static MutableComponent dataGet(String path, BlockPos pos, double scale, int value) {
            return dataGet(path, pos.getX(), pos.getY(), pos.getZ(), scale, value);
        }

        static MutableComponent dataGet(String path, int x, int y, int z, double scale, int value) {
            return command("data", "get", path, x, y, z, String.format(Locale.ROOT, "%.2f", scale), value);
        }

        private static MutableComponent command(String name, String value) {
            return modText("commands." + name, value);
        }

        private static MutableComponent command(String name, String value, Object... args) {
            return modText("commands." + name, value, args);
        }
    }

    private static MutableComponent modText(String type, String value) {
        return Component.translatable(type + "." + StargateAPI.MOD_ID + "." + value);
    }

    private static MutableComponent modText(String type, String value, Object... args) {
        return Component.translatable(type + "." + StargateAPI.MOD_ID + "." + value, args);
    }

    private static MutableComponent item(String value) {
        return modText("item", value);
    }

    @SuppressWarnings("deprecation")
    private static MutableComponent item(Item item, String suffix) {
        return Component.translatable("item." + item.builtInRegistryHolder().key().location().toLanguageKey() + "." + suffix);
    }

    @SuppressWarnings("deprecation")
    private static MutableComponent item(Item item, String suffix, Object... args) {
        return Component.translatable("item." + item.builtInRegistryHolder().key().location().toLanguageKey() + "." + suffix, args);
    }

    static MutableComponent itemGroup(String name) {
        return modText("itemGroup", name);
    }
}
