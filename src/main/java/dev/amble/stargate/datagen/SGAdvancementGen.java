package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.advancement.AmbleAdvancementProvider;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateCriterions;
import dev.amble.stargate.init.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;

public class SGAdvancementGen extends AmbleAdvancementProvider {

    public SGAdvancementGen(FabricDataOutput output) {
        super(output);

        Advancement root = create("root").icon(StargateBlocks.GENERIC_GATE)
                .noToast().silent().background("textures/block/raw_naquadah_block.png")
                .condition("root", InventoryChangedCriterion.Conditions.items(
                        StargateItems.ORLIN_STARGATE, StargateItems.DESTINY_STARGATE,
                        StargateItems.MILKY_WAY_STARGATE, StargateItems.PEGASUS_STARGATE
                )).build();

        Advancement rawNaquadah = challenge(root, "obtain_raw_naquadah").icon(StargateItems.RAW_NAQUADAH)
                .condition("obtain_raw_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.RAW_NAQUADAH))
                .hidden().build();

        Advancement addressCartouche = goal(root, "obtain_address_cartouche").icon(StargateItems.ADDRESS_CARTOUCHE)
                .condition("obtain_address_cartouche", InventoryChangedCriterion.Conditions.items(StargateItems.ADDRESS_CARTOUCHE))
                .hidden().build();

        Advancement liquidNaquadah = goal(rawNaquadah, "obtain_liquid_naquadah").icon(StargateItems.LIQUID_NAQUADAH)
                .condition("obtain_liquid_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.LIQUID_NAQUADAH))
                .hidden().build();

        Advancement toaster = goal(root, "obtain_toaster").icon(StargateBlocks.TOASTER)
                .condition("obtain_toaster", InventoryChangedCriterion.Conditions.items(StargateBlocks.TOASTER))
                .hidden().build();

        Advancement burntToast = challenge(toaster, "obtain_burnt_toast").icon(StargateItems.BURNT_TOAST)
                .condition("obtain_burnt_toast", InventoryChangedCriterion.Conditions.items(StargateItems.BURNT_TOAST))
                .hidden().build();

        Advancement passedThrough = challenge(root, "passed_through").icon(StargateItems.DESTINY_STARGATE)
                .condition("has_passed_through", StargateCriterions.PASSED_THROUGH.conditions()).build();
    }
}
