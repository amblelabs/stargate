package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleModelProvider;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

// TODO: pass suppliers instead
public class FabricStargateModelProvider extends FabricAmbleModelProvider {

    public FabricStargateModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(AmbleBlockModelGenerators gen) {
        // TODO: figure out how to datagen blockstate with direction...
        gen.blockEntityModels(StargateBlocks.STARGATE, StargateBlocks.NAQUADAH_BLOCK);
        gen.blockEntityModels(StargateBlocks.RING, StargateBlocks.NAQUADAH_BLOCK);

        gen.createTrivialCube(StargateBlocks.NAQUADAH_ORE);
        gen.createTrivialCube(StargateBlocks.RAW_NAQUADAH_BLOCK);
        gen.createTrivialCube(StargateBlocks.NAQUADAH_BLOCK);

        gen.family(StargateBlocks.SANDSTONE_BRICKS)
                .slab(StargateBlocks.SANDSTONE_BRICK_SLAB)
                .stairs(StargateBlocks.SANDSTONE_BRICK_STAIRS)
                .wall(StargateBlocks.SANDSTONE_BRICK_WALL);

        gen.createCrossBlockWithDefaultItem(StargateBlocks.DRY_BUSH, BlockModelGenerators.TintState.NOT_TINTED);
        gen.createCrossBlockWithDefaultItem(StargateBlocks.DRY_GRASS, BlockModelGenerators.TintState.NOT_TINTED);

        gen.createNonTemplateHorizontalBlock(StargateBlocks.TOASTER);
    }

    @Override
    public void generateItemModels(AmbleItemModelGenerators gen) {
        gen.generateFlatItem(StargateItems.TRINIUM_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NETHERITE_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.DIAMOND_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.GOLD_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.IRON_IRIS, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateBlocks.TOASTER, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.ADDRESS_CARTOUCHE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.TOAST, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.BURNT_TOAST, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_NUGGET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.RAW_NAQUADAH, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.CRYSTAL_INGOT, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.MUSIC_DISC_THEME, ModelTemplates.FLAT_ITEM);
    }
}