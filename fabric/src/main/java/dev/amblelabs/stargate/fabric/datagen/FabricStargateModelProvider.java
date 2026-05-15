package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.lib.fabric.datagen.FabricAmbleModelProvider;
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
import net.minecraft.world.level.block.Blocks;

public class FabricStargateModelProvider extends FabricAmbleModelProvider {

    public FabricStargateModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.blockEntityModels(StargateBlocks.STARGATE, Blocks.IRON_BLOCK);

        createToaster(gen, StargateBlocks.TOASTER);
    }

    private static void createToaster(BlockModelGenerators generator, Block horizontalBlock) {
        ResourceLocation model = ModelLocationUtils.getModelLocation(horizontalBlock, "");

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(horizontalBlock, Variant.variant().with(VariantProperties.MODEL, model))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(StargateBlocks.TOASTER.asItem(), ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TOAST, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.BURNT_TOAST, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_INGOT, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.CRYSTAL_INGOT, ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(StargateItems.TRINIUM_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NAQUADAH_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.NETHERITE_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.DIAMOND_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.GOLD_IRIS, ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(StargateItems.IRON_IRIS, ModelTemplates.FLAT_ITEM);
    }
}
