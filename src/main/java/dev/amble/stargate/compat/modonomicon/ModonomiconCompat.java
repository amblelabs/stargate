package dev.amble.stargate.compat.modonomicon;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.datagen.util.AutomaticModel;
import dev.amble.lib.datagen.util.NoEnglish;
import dev.amble.lib.item.AItemSettings;
import dev.amble.stargate.compat.Compat;
import dev.amble.stargate.init.StargateItemGroups;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.jetbrains.annotations.Nullable;

public class ModonomiconCompat implements ModInitializer {

    @Override
    public void onInitialize() {
        Compat.items.add(Items.class);
    }

    public static class Items extends ItemContainer {

        @AutomaticModel
        @NoEnglish
        public static final Item GUIDE_BOOK = new GuideBookItem(new AItemSettings());

        @Override
        public @Nullable ItemGroup getDefaultGroup() {
            return StargateItemGroups.MAIN;
        }
    }
}
