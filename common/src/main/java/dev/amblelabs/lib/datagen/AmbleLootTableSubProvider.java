package dev.amblelabs.lib.datagen;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class AmbleLootTableSubProvider implements LootTableSubProvider {

    protected final String modId;

    protected AmbleLootTableSubProvider(String modId) {
        this.modId = modId;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> register) {
        var blockTables = new HashMap<Block, LootTable.Builder>();
        var lootTables = new HashMap<ResourceKey<LootTable>, LootTable.Builder>();

        this.makeLootTables(blockTables, lootTables);

        for (var entry : blockTables.entrySet()) {
            register.accept(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootContextParamSets.BLOCK));
        }

        lootTables.forEach(register);
    }

    @SuppressWarnings({"EmptyMethod", "unused"})
    protected abstract void makeLootTables(Map<Block, LootTable.Builder> blockTables,
                                           Map<ResourceKey<LootTable>, LootTable.Builder> lootTables);

    protected LootPool.Builder dropThisPool(ItemLike item, int count) {
        return dropThisPool(item, ConstantValue.exactly(count));
    }

    protected LootPool.Builder dropThisPool(ItemLike item, NumberProvider count) {
        return LootPool.lootPool()
            .setRolls(count)
            .add(LootItem.lootTableItem(item));
    }

    @SafeVarargs
    protected final void dropSelf(Map<Block, LootTable.Builder> lootTables, Supplier<? extends Block>... blocks) {
        for (var blockSupp : blocks) {
            var block = blockSupp.get();
            dropSelf(block, lootTables);
        }
    }

    protected void dropSelf(Map<Block, LootTable.Builder> lootTables, Block... blocks) {
        for (var block : blocks) {
            dropSelf(block, lootTables);
        }
    }

    protected void dropSelf(Block block, Map<Block, LootTable.Builder> lootTables) {
        var table = LootTable.lootTable().withPool(dropThisPool(block, 1));
        lootTables.put(block, table);
    }

    protected void dropThis(Block block, ItemLike drop, Map<Block, LootTable.Builder> lootTables) {
        var table = LootTable.lootTable().withPool(dropThisPool(drop, 1));
        lootTables.put(block, table);
    }

    protected void dropThis(Block block, ItemLike drop, NumberProvider count,
        Map<Block, LootTable.Builder> lootTables) {
        var table = LootTable.lootTable().withPool(dropThisPool(drop, count));
        lootTables.put(block, table);
    }
}