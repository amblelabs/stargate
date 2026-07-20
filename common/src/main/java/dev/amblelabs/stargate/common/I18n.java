package dev.amblelabs.stargate.common;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public interface I18n {
    Component GENERIC_SHIFT_TOOLTIP = item("generic.tooltip.shift");
    Component DIALER_TOOLTIP = item(StargateItems.DIALER, "tooltip");
    Component DIALER_FAIL = item(StargateItems.DIALER, "fail");

    private static Component modText(String type, String value) {
        return Component.translatable(type + "." + StargateAPI.MOD_ID + "." + value);
    }

    private static Component modText(String type, String value, Object... args) {
        return Component.translatable(type + "." + StargateAPI.MOD_ID + "." + value, args);
    }

    private static Component item(String value) {
        return modText("item", value);
    }

    @SuppressWarnings("deprecation")
    private static Component item(Item item, String suffix) {
        return Component.translatable(item.builtInRegistryHolder().key().location().toLanguageKey() + "." + suffix);
    }
}
