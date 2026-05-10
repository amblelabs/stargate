package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleModelProvider;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.level.block.Blocks;

public class FabricStargateModelProvider extends FabricAmbleModelProvider {

    public FabricStargateModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.blockEntityModels(StargateBlocks.STARGATE_BLOCK, Blocks.IRON_BLOCK);
        gen.createTrivialCube(StargateBlocks.NAQUADAH_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(StargateItems.NAQUADAH_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_NUGGET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.RAW_NAQUADAH, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.CRYSTAL_INGOT, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NETHERITE_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.DIAMOND_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.GOLD_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.IRON_IRIS, ModelTemplates.FLAT_ITEM);
    }
}
