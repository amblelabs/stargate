package dev.amblelabs.stargate.datagen;

import dev.amblelabs.lib.datagen.AmbleAdvancementSubProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;

import java.util.function.Consumer;

public class StargateAdvancements extends AmbleAdvancementSubProvider {

    public StargateAdvancements() {
        super(StargateAPI.MOD_ID);
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        // TODO: make this trigger just once - on join.
        AdvancementHolder root = create("root").icon(StargateBlocks.STARGATE)
                .noToast().silent().background("textures/block/raw_naquadah_block.png")
                .condition("root", PlayerTrigger.TriggerInstance.tick())
                .rewards(AdvancementRewards.Builder.function(StargateAPI.modLoc("welcome")))
                .build(consumer);

        AdvancementHolder rawNaquadah = challenge(root, "raw_naquadah").icon(StargateItems.RAW_NAQUADAH)
                .condition("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(StargateItems.RAW_NAQUADAH))
                .hidden().build(consumer);

        AdvancementHolder addressCartouche = goal(root, "address_cartouche").icon(StargateItems.ADDRESS_CARTOUCHE)
                .condition("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(StargateItems.ADDRESS_CARTOUCHE))
                .hidden().build(consumer);

//        AdvancementHolder liquidNaquadah = goal(rawNaquadah, "obtain_liquid_naquadah").icon(StargateItems.LIQUID_NAQUADAH)
//                .condition("obtain_liquid_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.LIQUID_NAQUADAH))
//                .hidden().build();

        AdvancementHolder toaster = goal(root, "toaster").icon(StargateBlocks.TOASTER)
                .condition("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(StargateBlocks.TOASTER))
                .hidden().build(consumer);

        AdvancementHolder burntToast = challenge(toaster, "burn_toast").icon(StargateItems.BURNT_TOAST)
                .condition("burn_toast", InventoryChangeTrigger.TriggerInstance.hasItems(StargateItems.BURNT_TOAST))
                .hidden().build(consumer);

//        AdvancementHolder passedThrough = challenge(root, "passed_through").icon(StargateBlocks.STARGATE)
//                .condition("has_passed_through", StargateCriterions.PASSED_THROUGH.conditions()).build();

//        AdvancementHolder goldenIris = goal(root, "golden_iris").icon(StargateItems.GOLD_IRIS)
//                .condition("was_broken", BreakIrisCriterion.Conditions.create(StargateIrisTiers.GOLD))
//                .build();
    }
}