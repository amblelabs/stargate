package dev.amblelabs.lib.datagen;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "SameParameterValue", "unused"})
public abstract class AmbleAdvancementSubProvider implements AdvancementSubProvider {

    protected final String modId;

    protected AmbleAdvancementSubProvider(String modId) {
        this.modId = modId;
    }

    protected DisplayInfo simple(ItemLike icon, String name, AdvancementType frameType) {
        return simpleBg(icon, name, frameType, Optional.empty());
    }

    protected DisplayInfo simpleBg(ItemLike icon, String name, AdvancementType frameType, Optional<ResourceLocation> background) {
        return display(new ItemStack(icon), name, frameType, background, true, true, false);
    }

    protected DisplayInfo display(ItemStack icon, String name, AdvancementType frameType, Optional<ResourceLocation> background, boolean showToast, boolean announceChat, boolean hidden) {
        name = "advancement." + this.modId + ":" + name;

        return new DisplayInfo(icon,
            Component.translatable(name),
            Component.translatable(name + ".desc"),
            background, frameType, showToast, announceChat, hidden);
    }

    protected ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(modId, name);
    }
}