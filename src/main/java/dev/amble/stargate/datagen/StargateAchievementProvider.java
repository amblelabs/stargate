package dev.amble.stargate.datagen;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateCriterions;
import dev.amble.stargate.init.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
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
                .display(StargateBlocks.GENERIC_GATE,
                        Text.translatable("achievement.stargate.title.root"),
                        Text.translatable("achievement.stargate.description.root"),
                        new Identifier("stargate", "textures/block/raw_naquadah_block.png"),
                        AdvancementFrame.TASK, false, false, false)
                .criterion("root", InventoryChangedCriterion.Conditions.items(StargateBlocks.GENERIC_GATE))
                .build(consumer, StargateMod.MOD_ID + ":root");

        Advancement rawNaquadah = Advancement.Builder.create().parent(root)
                .display(StargateItems.RAW_NAQUADAH,
                        Text.translatable("achievement.stargate.title.obtain_raw_naquadah"),
                        Text.translatable("achievement.stargate.description.obtain_raw_naquadah"),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("obtain_raw_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.RAW_NAQUADAH))
                .build(consumer, StargateMod.MOD_ID + ":obtain_raw_naquadah");

        Advancement addressCartouche = Advancement.Builder.create().parent(root)
                .display(StargateItems.ADDRESS_CARTOUCHE,
                        Text.translatable("achievement.stargate.title.obtain_address_cartouche"),
                        Text.translatable("achievement.stargate.description.obtain_address_cartouche"),
                        null, AdvancementFrame.GOAL, true, true, true)
                .criterion("obtain_address_cartouche", InventoryChangedCriterion.Conditions.items(StargateItems.ADDRESS_CARTOUCHE))
                .build(consumer, StargateMod.MOD_ID + ":obtain_address_cartouche");

        Advancement liquidNaquadah = Advancement.Builder.create().parent(rawNaquadah)
                .display(StargateItems.LIQUID_NAQUADAH,
                        Text.translatable("achievement.stargate.title.obtain_liquid_naquadah"),
                        Text.translatable("achievement.stargate.description.obtain_liquid_naquadah"),
                        null, AdvancementFrame.GOAL, true, true, true)
                .criterion("obtain_liquid_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.LIQUID_NAQUADAH))
                .build(consumer, StargateMod.MOD_ID + ":obtain_liquid_naquadah");

        Advancement toaster = Advancement.Builder.create().parent(root)
                .display(StargateBlocks.TOASTER,
                        Text.translatable("achievement.stargate.title.obtain_toaster"),
                        Text.translatable("achievement.stargate.description.obtain_toaster"),
                        null, AdvancementFrame.GOAL, true, true, true)
                .criterion("obtain_toaster", InventoryChangedCriterion.Conditions.items(StargateBlocks.TOASTER))
                .build(consumer, StargateMod.MOD_ID + ":obtain_toaster");

        Advancement burnt_toast = Advancement.Builder.create().parent(toaster)
                .display(StargateItems.BURNT_TOAST,
                        Text.translatable("achievement.stargate.title.obtain_burnt_toast"),
                        Text.translatable("achievement.stargate.description.obtain_burnt_toast"),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("obtain_burnt_toast", InventoryChangedCriterion.Conditions.items(StargateItems.BURNT_TOAST))
                .build(consumer, StargateMod.MOD_ID + ":obtain_burnt_toast");

        Advancement passedThrough = new Builder(root, "passed_through", consumer).icon(StargateItems.DESTINY_STARGATE)
                .condition("has_passed_through", StargateCriterions.PASSED_THROUGH.conditions())
                .frame(AdvancementFrame.CHALLENGE).build();
    }

    static class Builder {

        private final Advancement.Builder builder;
        private final Consumer<Advancement> consumer;

        private ItemConvertible item = Items.BARRIER;
        private boolean hidden = false;
        private AdvancementFrame frame = AdvancementFrame.TASK;
        private boolean announce = true;
        private boolean showToast = true;

        private final String name;

        public Builder(Advancement parent, String name, Consumer<Advancement> builder) {
            this.builder = Advancement.Builder.create().parent(parent);
            this.consumer = builder;
            this.name = name;
        }

        public Builder condition(String name, CriterionConditions conditions) {
            this.builder.criterion(name, conditions);
            return this;
        }

        public Builder icon(ItemConvertible item) {
            this.item = item;
            return this;
        }

        public Builder hidden() {
            this.hidden = true;
            return this;
        }

        public Builder frame(AdvancementFrame frame) {
            this.frame = frame;
            return this;
        }

        public Builder silent() {
            this.announce = false;
            return this;
        }

        public Builder noToast() {
            this.showToast = false;
            return this;
        }

        public Advancement build() {
            return builder
                    .display(item,
                            Text.translatable("achievement." + StargateMod.MOD_ID + ".title." + name),
                            Text.translatable("achievement." + StargateMod.MOD_ID + ".description." + name),
                            null, frame, showToast, announce, hidden)
                    .build(this.consumer, StargateMod.MOD_ID + ":" + name);
        }
    }
}