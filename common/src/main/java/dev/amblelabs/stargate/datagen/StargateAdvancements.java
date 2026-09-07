package dev.amblelabs.stargate.datagen;

import dev.amblelabs.lib.datagen.AmbleAdvancementSubProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.advancements.*;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

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

        AdvancementHolder passedThrough = challenge(root, "passed_through").icon(StargateBlocks.STARGATE)
                .condition("has_passed_through", PassedThroughTrigger.TriggerInstance.passedThrough()).build(consumer);

        AdvancementHolder diedToIris = goal(passedThrough, "death_iris").icon(StargateItems.NETHERITE_IRIS)
                .condition("died", IrisDamageTrigger.TriggerInstance.dead()).build(consumer);

        AdvancementHolder diedToFlow = goal(passedThrough, "death_flow").icon(Blocks.SKELETON_SKULL)
                .condition("died", FlowDamageTrigger.TriggerInstance.dead()).build(consumer);

        AdvancementHolder diedToKawoosh = goal(passedThrough, "death_kawoosh").icon(Blocks.SKELETON_SKULL)
                .condition("died", KawooshDamageTrigger.TriggerInstance.dead()).build(consumer);

        AdvancementHolder goldenIris = challenge(root, "golden_iris").icon(StargateItems.GOLD_IRIS)
                .condition("was_broken", BreakIrisTrigger.TriggerInstance.broken(IrisItem.Type.GOLD))
                .build(consumer);

        AdvancementHolder activation = goal(StargateAPI.modLoc("find_gate"), "activate")
                .condition("dialed", StargateDialTrigger.TriggerInstance.dialed(MinMaxBounds.Ints.atLeast(7)))
                .build(consumer);

        AdvancementHolder c8 = goal(activation, "c8")
                .condition("dialed", StargateDialTrigger.TriggerInstance.dialed(MinMaxBounds.Ints.exactly(8)))
                .build(consumer);

        AdvancementHolder c9 = goal(c8, "c9")
                .condition("dialed", StargateDialTrigger.TriggerInstance.dialed(MinMaxBounds.Ints.exactly(9)))
                .build(consumer);

        // defined manually because of datagen being gay
//        AdvancementHolder findBuriedGate = goal(root, "find_gate").icon(StargateBlocks.STARGATE)
//                .condition("found", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(
//                        provider.lookupOrThrow(Registries.STRUCTURE).get(ResourceKey.create(Registries.STRUCTURE, StargateAPI.modLoc("buried_stargate"))).orElseThrow())))
//                .build(consumer);
    }
}