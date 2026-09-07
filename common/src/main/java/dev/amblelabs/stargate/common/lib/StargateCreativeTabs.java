package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.I18n;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

public class StargateCreativeTabs {

    private static final XplatRegister<CreativeModeTab> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.CREATIVE_MODE_TAB);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Holder<CreativeModeTab> STARGATE = tab("main", CreativeModeTab.Row.TOP, 7,
            tab -> tab.icon(() -> new ItemStack(StargateBlocks.STARGATE)));

    public static final ResourceKey<CreativeModeTab> STARGATE_KEY = key(STARGATE);

    @SuppressWarnings("SameParameterValue")
    private static Holder<CreativeModeTab> tab(String name, CreativeModeTab.Row row, int column, UnaryOperator<CreativeModeTab.Builder> op) {
        return REGISTER.registerHolder(name, () -> op.apply(CreativeModeTab.builder(row, column).title(I18n.itemGroup(name))).build());
    }

    @SuppressWarnings({"SameParameterValue", "OptionalGetWithoutIsPresent"})
    private static ResourceKey<CreativeModeTab> key(Holder<CreativeModeTab> holder) {
        return holder.unwrapKey().get();
    }
}