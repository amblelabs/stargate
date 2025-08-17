package dev.amble.stargate.datagen;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.core.fluid.StargateFluids;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class StargateAchievementProvider extends FabricAdvancementProvider {
    public StargateAchievementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement root = Advancement.Builder.create()
                .display(StargateBlocks.STARGATE,
                        Text.translatable("achievement.stargate.title.root"),
                        Text.translatable("achievement.stargate.description.root"),
                        new Identifier("stargate", "textures/block/raw_naquadah_block.png"),
                        AdvancementFrame.TASK, false, false, false)
                .criterion("root", InventoryChangedCriterion.Conditions.items(StargateBlocks.STARGATE))
                .build(consumer, StargateMod.MOD_ID + "/root");

        Advancement rawNaquadah = Advancement.Builder.create().parent(root)
                .display(StargateItems.RAW_NAQUADAH,
                        Text.translatable("achievement.stargate.title.obtain_raw_naquadah"),
                        Text.translatable("achievement.stargate.description.obtain_raw_naquadah"),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("obtain_raw_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.RAW_NAQUADAH))
                .build(consumer, StargateMod.MOD_ID + "/obtain_raw_naquadah");

        Advancement addressCartouche = Advancement.Builder.create().parent(root)
                .display(StargateItems.ADDRESS_CARTOUCHE,
                        Text.translatable("achievement.stargate.title.obtain_address_cartouche"),
                        Text.translatable("achievement.stargate.description.obtain_address_cartouche"),
                        null, AdvancementFrame.GOAL, true, true, true)
                .criterion("obtain_address_cartouche", InventoryChangedCriterion.Conditions.items(StargateItems.ADDRESS_CARTOUCHE))
                .build(consumer, StargateMod.MOD_ID + "/obtain_address_cartouche");

        Advancement liquidNaquadah = Advancement.Builder.create().parent(rawNaquadah)
                .display(StargateFluids.LIQUID_NAQUADAH,
                        Text.translatable("achievement.stargate.title.obtain_liquid_naquadah"),
                        Text.translatable("achievement.stargate.description.obtain_liquid_naquadah"),
                        null, AdvancementFrame.GOAL, true, true, true)
                .criterion("obtain_liquid_naquadah", InventoryChangedCriterion.Conditions.items(StargateFluids.LIQUID_NAQUADAH))
                .build(consumer, StargateMod.MOD_ID + "/obtain_liquid_naquadah");
    }
}