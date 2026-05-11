package dev.amblelabs.stargate.datagen;

import dev.amblelabs.lib.datagen.AmbleLootTableSubProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Map;

@SuppressWarnings("unused")
public class StargateLootTables extends AmbleLootTableSubProvider {

    public StargateLootTables() {
        super(StargateAPI.MOD_ID);
    }

    @Override
    protected void makeLootTables(Map<Block, LootTable.Builder> blockTables, Map<ResourceKey<LootTable>, LootTable.Builder> lootTables) {

        dropSelf(blockTables, StargateBlocks.NAQUADAH_BLOCK);
        dropSelf(blockTables, StargateBlocks.RAW_NAQUADAH_BLOCK);
    }



    private void makeSlabTable(Map<Block, LootTable.Builder> lootTables, Block block) {
        var leafPool = dropThisPool(block, 1)
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                .when(new LootItemBlockStatePropertyCondition.Builder(block).setProperties(
                    StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)
                )))
            .apply(ApplyExplosionDecay.explosionDecay());
        lootTables.put(block, LootTable.lootTable().withPool(leafPool));
    }
}