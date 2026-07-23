package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleModelProvider;
import dev.amblelabs.stargate.common.items.StargateBlockItem;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class FabricStargateModelProvider extends FabricAmbleModelProvider {

    public FabricStargateModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        // TODO: figure out how to datagen blockstate with direction...
        gen.blockEntityModels(StargateBlocks.STARGATE, StargateBlocks.NAQUADAH_BLOCK);

        gen.createTrivialCube(StargateBlocks.NAQUADAH_ORE);
        gen.createTrivialCube(StargateBlocks.RAW_NAQUADAH_BLOCK);
        gen.createTrivialCube(StargateBlocks.NAQUADAH_BLOCK);

        gen.family(StargateBlocks.SANDSTONE_BRICKS)
                .slab(StargateBlocks.SANDSTONE_BRICK_SLAB)
                .stairs(StargateBlocks.SANDSTONE_BRICK_STAIRS)
                .wall(StargateBlocks.SANDSTONE_BRICK_WALL);

        gen.createCrossBlockWithDefaultItem(StargateBlocks.DRY_BUSH, BlockModelGenerators.TintState.NOT_TINTED);
        gen.createCrossBlockWithDefaultItem(StargateBlocks.DRY_GRASS, BlockModelGenerators.TintState.NOT_TINTED);

        createToaster(gen, StargateBlocks.TOASTER);
    }

    private static void createToaster(BlockModelGenerators generator, Block horizontalBlock) {
        ResourceLocation model = ModelLocationUtils.getModelLocation(horizontalBlock, "");

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(horizontalBlock, Variant.variant()
                        .with(VariantProperties.MODEL, model))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(StargateBlocks.STARGATE.asItem(), ModelTemplates.FLAT_ITEM);

        StargateItems.registerItems((item, resourceLocation) -> {
            if (item instanceof StargateBlockItem)
                gen.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        });

        gen.generateFlatItem(StargateBlocks.TOASTER.asItem(), ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.ADDRESS_CARTOUCHE, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.TOAST, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.BURNT_TOAST, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_NUGGET, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.RAW_NAQUADAH, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.CRYSTAL_INGOT, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NETHERITE_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.DIAMOND_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.GOLD_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.IRON_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.MUSIC_DISC_THEME, ModelTemplates.FLAT_ITEM);
    }
}