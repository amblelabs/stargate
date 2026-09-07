package dev.amblelabs.lib.fabric.datagen;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class FabricAmbleModelProvider extends FabricModelProvider {

    public FabricAmbleModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        this.generateBlockStateModels(new AmbleBlockModelGenerators(gen));
    }
    
    public abstract void generateBlockStateModels(AmbleBlockModelGenerators gen);

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        this.generateItemModels(new AmbleItemModelGenerators(gen));
    }
    
    public abstract void generateItemModels(AmbleItemModelGenerators gen);
    
    public static class AmbleItemModelGenerators extends ItemModelGenerators {

        public AmbleItemModelGenerators(ItemModelGenerators gen) {
            this(gen.output);
        }
        
        public AmbleItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
            super(output);
        }

        public void generateFlatItem(ItemLike item, ModelTemplate modelTemplate) {
            this.generateFlatItem(item.asItem(), modelTemplate);
        }

        public void generateFlatItem(ItemLike item, String modelLocationSuffix, ModelTemplate modelTemplate) {
            this.generateFlatItem(item.asItem(), modelLocationSuffix, modelTemplate);
        }

        public void generateFlatItem(ItemLike item, ItemLike layerZeroItem, ModelTemplate modelTemplate) {
            this.generateFlatItem(item.asItem(), layerZeroItem.asItem(), modelTemplate);
        }

        public void generateItemWithOverlay(ItemLike item) {
            this.generateItemWithOverlay(item.asItem());
        }

        public void generateCompassItem(ItemLike item) {
            this.generateCompassItem(item.asItem());
        }

        public void generateClockItem(ItemLike item) {
            this.generateClockItem(item.asItem());
        }
    }
    
    @SuppressWarnings("UnusedReturnValue")
    public static class AmbleBlockModelGenerators extends BlockModelGenerators {

        public AmbleBlockModelGenerators(BlockModelGenerators gen) {
            super(gen.blockStateOutput, gen.modelOutput, gen.skippedAutoModelsOutput);
        }
        
        public void skipAutoItemBlock(Supplier<? extends Block> block) {
            this.skipAutoItemBlock(block.get());
        }

        public void delegateItemModel(Supplier<? extends Block> block, ResourceLocation delegateModelLocation) {
            this.delegateItemModel(block.get(), delegateModelLocation);
        }

        public void delegateItemModel(ItemLike item, ResourceLocation delegateModelLocation) {
            this.delegateItemModel(item.asItem(), delegateModelLocation);
        }

        public void createSimpleFlatItemModel(ItemLike flatItem) {
            this.createSimpleFlatItemModel(flatItem.asItem());
        }

        public void createSimpleFlatItemModel(Supplier<? extends Block> flatBlock) {
            this.createSimpleFlatItemModel(flatBlock.get());
        }

        public void createSimpleFlatItemModel(Supplier<? extends Block> flatBlock, String layerZeroTextureSuffix) {
            this.createSimpleFlatItemModel(flatBlock.get(), layerZeroTextureSuffix);
        }
        
        public static MultiVariantGenerator createRotatedVariant(Supplier<? extends Block> block, ResourceLocation normalModelLocation, ResourceLocation mirroredModelLocation) {
            return BlockModelGenerators.createRotatedVariant(block.get(), normalModelLocation, mirroredModelLocation);
        }

        public void createRotatedMirroredVariantBlock(Supplier<? extends Block> block) {
            this.createRotatedMirroredVariantBlock(block.get());
        }

        public void createRotatedVariantBlock(Supplier<? extends Block> block) {
            this.createRotatedVariantBlock(block.get());
        }

        public void createBrushableBlock(Supplier<? extends Block> block) {
            this.createBrushableBlock(block.get());
        }

        public static BlockStateGenerator createButton(Supplier<? extends Block> buttonBlock, ResourceLocation unpoweredModelLocation, ResourceLocation poweredModelLocation) {
            return BlockModelGenerators.createButton(buttonBlock.get(), unpoweredModelLocation, poweredModelLocation);
        }

        public static BlockStateGenerator createDoor(Supplier<? extends Block> doorBlock, ResourceLocation topLeftModelLocation, ResourceLocation topLeftOpenModelLocation, ResourceLocation topRightModelLocation, ResourceLocation topRightOpenModelLocation, ResourceLocation bottomLeftModelLocation, ResourceLocation bottomLeftOpenModelLocation, ResourceLocation bottomRightModelLocation, ResourceLocation bottomRightOpenModelLocation) {
            return BlockModelGenerators.createDoor(doorBlock.get(), topLeftModelLocation, topLeftOpenModelLocation, topRightModelLocation, topRightOpenModelLocation, bottomLeftModelLocation, bottomLeftOpenModelLocation, bottomRightModelLocation, bottomRightOpenModelLocation);
        }

        public static BlockStateGenerator createCustomFence(Supplier<? extends Block> customFenceBlock, ResourceLocation postModelId, ResourceLocation northModelId, ResourceLocation eastModelId, ResourceLocation southModelId, ResourceLocation westModelId) {
            return BlockModelGenerators.createCustomFence(customFenceBlock.get(), postModelId, northModelId, eastModelId, southModelId, westModelId);
        }

        public static BlockStateGenerator createFence(Supplier<? extends Block> fenceBlock, ResourceLocation fencePostModelLocation, ResourceLocation fenceSideModelLocation) {
            return BlockModelGenerators.createFence(fenceBlock.get(), fencePostModelLocation, fenceSideModelLocation);
        }

        public static BlockStateGenerator createWall(Supplier<? extends Block> wallBlock, ResourceLocation postModelLocation, ResourceLocation lowSideModelLocation, ResourceLocation tallSideModelLocation) {
            return BlockModelGenerators.createWall(wallBlock.get(), postModelLocation, lowSideModelLocation, tallSideModelLocation);
        }

        public static BlockStateGenerator createFenceGate(Supplier<? extends Block> fenceGateBlock, ResourceLocation openModelLocation, ResourceLocation closedModelLocation, ResourceLocation wallOpenModelLocation, ResourceLocation wallClosedModelLocation, boolean uvLock) {
            return BlockModelGenerators.createFenceGate(fenceGateBlock.get(), openModelLocation, closedModelLocation, wallOpenModelLocation, wallClosedModelLocation, uvLock);
        }

        public static BlockStateGenerator createStairs(Supplier<? extends Block> stairsBlock, ResourceLocation innerModelLocation, ResourceLocation straightModelLocation, ResourceLocation outerModelLocation) {
            return BlockModelGenerators.createStairs(stairsBlock.get(), innerModelLocation, straightModelLocation, outerModelLocation);
        }

        public static BlockStateGenerator createOrientableTrapdoor(Supplier<? extends Block> orientableTrapdoorBlock, ResourceLocation topModelLocation, ResourceLocation bottomModelLocation, ResourceLocation openModelLocation) {
            return BlockModelGenerators.createOrientableTrapdoor(orientableTrapdoorBlock.get(), topModelLocation, bottomModelLocation, openModelLocation);
        }

        public static BlockStateGenerator createTrapdoor(Supplier<? extends Block> trapdoorBlock, ResourceLocation topModelLocation, ResourceLocation bottomModelLocation, ResourceLocation openModelLocation) {
            return BlockModelGenerators.createTrapdoor(trapdoorBlock.get(), topModelLocation, bottomModelLocation, openModelLocation);
        }

        public static MultiVariantGenerator createSimpleBlock(Supplier<? extends Block> block, ResourceLocation modelLocation) {
            return BlockModelGenerators.createSimpleBlock(block.get(), modelLocation);
        }

        public static BlockStateGenerator createPillarBlockUVLocked(Supplier<? extends Block> block, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
            return BlockModelGenerators.createPillarBlockUVLocked(block.get(), textureMapping, modelOutput);
        }

        public static BlockStateGenerator createAxisAlignedPillarBlock(Supplier<? extends Block> axisAlignedPillarBlock, ResourceLocation modelLocation) {
            return BlockModelGenerators.createAxisAlignedPillarBlock(axisAlignedPillarBlock.get(), modelLocation);
        }

        public void createAxisAlignedPillarBlockCustomModel(Supplier<? extends Block> axisAlignedPillarBlock, ResourceLocation modelLocation) {
            this.createAxisAlignedPillarBlockCustomModel(axisAlignedPillarBlock.get(), modelLocation);
        }

        public void createAxisAlignedPillarBlock(Supplier<? extends Block> axisAlignedPillarBlock, TexturedModel.Provider provider) {
            this.createAxisAlignedPillarBlock(axisAlignedPillarBlock.get(), provider);
        }

        public void createHorizontallyRotatedBlock(Supplier<? extends Block> horizontallyRotatedBlock, TexturedModel.Provider provider) {
            this.createHorizontallyRotatedBlock(horizontallyRotatedBlock.get(), provider);
        }

        public static BlockStateGenerator createRotatedPillarWithHorizontalVariant(Supplier<? extends Block> rotatedPillarBlock, ResourceLocation modelLocation, ResourceLocation horizontalModelLocation) {
            return BlockModelGenerators.createRotatedPillarWithHorizontalVariant(rotatedPillarBlock.get(), modelLocation, horizontalModelLocation);
        }

        public void createRotatedPillarWithHorizontalVariant(Supplier<? extends Block> rotatedPillarBlock, TexturedModel.Provider modelProvider, TexturedModel.Provider horizontalModelProvider) {
            this.createRotatedPillarWithHorizontalVariant(rotatedPillarBlock.get(), modelProvider, horizontalModelProvider);
        }

        public ResourceLocation createSuffixedVariant(Supplier<? extends Block> block, String suffix, ModelTemplate modelTemplate, Function<ResourceLocation, TextureMapping> textureMappingGetter) {
            return this.createSuffixedVariant(block.get(), suffix, modelTemplate, textureMappingGetter);
        }

        public static BlockStateGenerator createPressurePlate(Supplier<? extends Block> pressurePlateBlock, ResourceLocation unpoweredModelLocation, ResourceLocation poweredModelLocation) {
            return BlockModelGenerators.createPressurePlate(pressurePlateBlock.get(), unpoweredModelLocation, poweredModelLocation);
        }

        public static BlockStateGenerator createSlab(Supplier<? extends Block> slabBlock, ResourceLocation bottomHalfModelLocation, ResourceLocation topHalfModelLocation, ResourceLocation doubleModelLocation) {
            return BlockModelGenerators.createSlab(slabBlock.get(), bottomHalfModelLocation, topHalfModelLocation, doubleModelLocation);
        }

        public void createTrivialCube(Supplier<? extends Block> block) {
            this.createTrivialCube(block.get());
        }

        public void createTrivialBlock(Supplier<? extends Block> block, TexturedModel.Provider provider) {
            this.createTrivialBlock(block.get(), provider);
        }

        public void createTrivialBlock(Supplier<? extends Block> block, TextureMapping textureMapping, ModelTemplate modelTemplate) {
            this.createTrivialBlock(block.get(), textureMapping, modelTemplate);
        }

        public AmbleBlockFamilyProvider family(Supplier<? extends Block> block) {
            return new AmbleBlockFamilyProvider(this.family(block.get()));
        }

        // Too many transmutations.
        public void createHangingSign(Supplier<? extends Block> particleBlock, Supplier<? extends Block> hangingSignBlock, Supplier<? extends Block> wallHangingSignBlock) {
            this.createHangingSign(particleBlock.get(), hangingSignBlock.get(), wallHangingSignBlock.get());
        }

        public void createDoor(Supplier<? extends Block> doorBlock) {
            this.createDoor(doorBlock.get());
        }

        public void copyDoorModel(Supplier<? extends Block> doorBlock, Supplier<? extends Block> sourceBlock) {
            this.copyDoorModel(doorBlock.get(), sourceBlock.get());
        }

        public void copyDoorModel(Block doorBlock, Supplier<? extends Block> sourceBlock) {
            this.copyDoorModel(doorBlock, sourceBlock.get());
        }

        public void copyDoorModel(Supplier<? extends Block> doorBlock, Block sourceBlock) {
            this.copyDoorModel(doorBlock.get(), sourceBlock);
        }

        public void createOrientableTrapdoor(Supplier<? extends Block> orientableTrapdoorBlock) {
            this.createOrientableTrapdoor(orientableTrapdoorBlock.get());
        }

        public void createTrapdoor(Supplier<? extends Block> trapdoorBlock) {
            this.createTrapdoor(trapdoorBlock.get());
        }

        public void copyTrapdoorModel(Supplier<? extends Block> trapdoorBlock, Supplier<? extends Block> sourceBlock) {
            this.copyTrapdoorModel(trapdoorBlock.get(), sourceBlock.get());
        }

        public void copyTrapdoorModel(Block trapdoorBlock, Supplier<? extends Block> sourceBlock) {
            this.copyTrapdoorModel(trapdoorBlock, sourceBlock.get());
        }

        public void copyTrapdoorModel(Supplier<? extends Block> trapdoorBlock, Block sourceBlock) {
            this.copyTrapdoorModel(trapdoorBlock.get(), sourceBlock);
        }

        public WoodProvider woodProvider(Supplier<? extends Block> logBlock) {
            return this.woodProvider(logBlock.get());
        }

        public void createNonTemplateModelBlock(Supplier<? extends Block> block) {
            this.createNonTemplateModelBlock(block.get());
        }

        public void createNonTemplateModelBlock(Supplier<? extends Block> block, Supplier<? extends Block> modelBlock) {
            this.createNonTemplateModelBlock(block.get(), modelBlock.get());
        }

        public void createNonTemplateModelBlock(Block block, Supplier<? extends Block> modelBlock) {
            this.createNonTemplateModelBlock(block, modelBlock.get());
        }

        public void createNonTemplateModelBlock(Supplier<? extends Block> block, Block modelBlock) {
            this.createNonTemplateModelBlock(block.get(), modelBlock);
        }

        public void createCrossBlockWithDefaultItem(Supplier<? extends Block> crossBlock, TintState tintState) {
            this.createCrossBlockWithDefaultItem(crossBlock.get(), tintState);
        }

        public void createCrossBlockWithDefaultItem(Supplier<? extends Block> crossBlock, TintState tintState, TextureMapping textureMapping) {
            this.createCrossBlockWithDefaultItem(crossBlock.get(), tintState, textureMapping);
        }

        public void createCrossBlock(Supplier<? extends Block> crossBlock, TintState tintState) {
            this.createCrossBlock(crossBlock.get(), tintState);
        }

        public void createCrossBlock(Supplier<? extends Block> crossBlock, TintState tintState, TextureMapping textureMapping) {
            this.createCrossBlock(crossBlock.get(), tintState, textureMapping);
        }

        public void createCrossBlock(Supplier<? extends Block> crossBlock, TintState tintState, Property<Integer> property, int... propertyValues) {
            this.createCrossBlock(crossBlock.get(), tintState, property, propertyValues);
        }

        public void createPlant(Supplier<? extends Block> plantBlock, Supplier<? extends Block> pottedPlantBlock, TintState tintState) {
            this.createPlant(plantBlock.get(), pottedPlantBlock.get(), tintState);
        }

        public void createPlant(Block plantBlock, Supplier<? extends Block> pottedPlantBlock, TintState tintState) {
            this.createPlant(plantBlock, pottedPlantBlock.get(), tintState);
        }

        public void createPlant(Supplier<? extends Block> plantBlock, Block pottedPlantBlock, TintState tintState) {
            this.createPlant(plantBlock.get(), pottedPlantBlock, tintState);
        }

        public void createCoralFans(Supplier<? extends Block> coralFanBlock, Supplier<? extends Block> coralWallFanBlock) {
            this.createCoralFans(coralFanBlock.get(), coralWallFanBlock.get());
        }

        public void createCoralFans(Block coralFanBlock, Supplier<? extends Block> coralWallFanBlock) {
            this.createCoralFans(coralFanBlock, coralWallFanBlock.get());
        }

        public void createCoralFans(Supplier<? extends Block> coralFanBlock, Block coralWallFanBlock) {
            this.createCoralFans(coralFanBlock.get(), coralWallFanBlock);
        }

        public void createStems(Supplier<? extends Block> unattachedStemBlock, Supplier<? extends Block> attachedStemBlock) {
            this.createStems(unattachedStemBlock.get(), attachedStemBlock.get());
        }

        public void createStems(Block unattachedStemBlock, Supplier<? extends Block> attachedStemBlock) {
            this.createStems(unattachedStemBlock, attachedStemBlock.get());
        }

        public void createStems(Supplier<? extends Block> unattachedStemBlock, Block attachedStemBlock) {
            this.createStems(unattachedStemBlock.get(), attachedStemBlock);
        }

        // Too many transmutations.
        public void createCoral(Supplier<? extends Block> coralBlock, Supplier<? extends Block> deadCoralBlock, Supplier<? extends Block> coralFullBlock, Supplier<? extends Block> deadCoralFullBlock, Supplier<? extends Block> coralFanBlock, Supplier<? extends Block> deadCoralFanBlock, Supplier<? extends Block> coralWallFanBlock, Supplier<? extends Block> deadCoralWallFanBlock) {
            this.createCoral(coralBlock.get(), deadCoralBlock.get(), coralFullBlock.get(), deadCoralFullBlock.get(), coralFanBlock.get(), deadCoralFanBlock.get(), coralWallFanBlock.get(), deadCoralWallFanBlock.get());
        }

        public void createDoublePlant(Supplier<? extends Block> doublePlantBlock, TintState tintState) {
            this.createDoublePlant(doublePlantBlock.get(), tintState);
        }

        public void createDoubleBlock(Supplier<? extends Block> doubleBlock, ResourceLocation topHalfModelLocation, ResourceLocation bottomHalfModelLocation) {
            this.createDoubleBlock(doubleBlock.get(), topHalfModelLocation, bottomHalfModelLocation);
        }

        public void createPassiveRail(Supplier<? extends Block> railBlock) {
            this.createPassiveRail(railBlock.get());
        }

        public void createActiveRail(Supplier<? extends Block> railBlock) {
            this.createActiveRail(railBlock.get());
        }

        public BlockEntityModelGenerator blockEntityModels(ResourceLocation entityBlockModelLocation, Supplier<? extends Block> particleBlock) {
            return this.blockEntityModels(entityBlockModelLocation, particleBlock.get());
        }

        public BlockEntityModelGenerator blockEntityModels(Supplier<? extends Block> entityBlockBaseModel, Supplier<? extends Block> particleBlock) {
            return this.blockEntityModels(entityBlockBaseModel.get(), particleBlock.get());
        }

        public BlockEntityModelGenerator blockEntityModels(Block entityBlockBaseModel, Supplier<? extends Block> particleBlock) {
            return this.blockEntityModels(entityBlockBaseModel, particleBlock.get());
        }

        public BlockEntityModelGenerator blockEntityModels(Supplier<? extends Block> entityBlockBaseModel, Block particleBlock) {
            return this.blockEntityModels(entityBlockBaseModel.get(), particleBlock);
        }

        public void createAirLikeBlock(Supplier<? extends Block> airLikeBlock, ItemLike particleItem) {
            this.createAirLikeBlock(airLikeBlock.get(), particleItem.asItem());
        }

        public void createAirLikeBlock(Block airLikeBlock, ItemLike particleItem) {
            this.createAirLikeBlock(airLikeBlock, particleItem.asItem());
        }

        public void createAirLikeBlock(Supplier<? extends Block> airLikeBlock, ResourceLocation particleTexture) {
            this.createAirLikeBlock(airLikeBlock.get(), particleTexture);
        }

        public void createFullAndCarpetBlocks(Supplier<? extends Block> fullBlock, Supplier<? extends Block> carpetBlock) {
            this.createFullAndCarpetBlocks(fullBlock.get(), carpetBlock.get());
        }

        public void createFullAndCarpetBlocks(Block fullBlock, Supplier<? extends Block> carpetBlock) {
            this.createFullAndCarpetBlocks(fullBlock, carpetBlock.get());
        }

        public void createFullAndCarpetBlocks(Supplier<? extends Block> fullBlock, Block carpetBlock) {
            this.createFullAndCarpetBlocks(fullBlock.get(), carpetBlock);
        }

        public void createFlowerBed(Supplier<? extends Block> flowerBedBlock) {
            this.createFlowerBed(flowerBedBlock.get());
        }

        @SafeVarargs
        public final void createColoredBlockWithRandomRotations(TexturedModel.Provider modelProvider, Supplier<? extends Block>... coloredBlocks) {
            this.createColoredBlockWithRandomRotations(modelProvider, Arrays.stream(coloredBlocks).map(Supplier::get).toArray(Block[]::new));
        }

        @SafeVarargs
        public final void createColoredBlockWithStateRotations(TexturedModel.Provider modelProvider, Supplier<? extends Block>... coloredBlocks) {
            this.createColoredBlockWithStateRotations(modelProvider, Arrays.stream(coloredBlocks).map(Supplier::get).toArray(Block[]::new));
        }

        public void createGlassBlocks(Supplier<? extends Block> glassBlock, Supplier<? extends Block> paneBlock) {
            this.createGlassBlocks(glassBlock.get(), paneBlock.get());
        }

        public void createGlassBlocks(Block glassBlock, Supplier<? extends Block> paneBlock) {
            this.createGlassBlocks(glassBlock, paneBlock.get());
        }

        public void createGlassBlocks(Supplier<? extends Block> glassBlock, Block paneBlock) {
            this.createGlassBlocks(glassBlock.get(), paneBlock);
        }

        public void createCommandBlock(Supplier<? extends Block> commandBlock) {
            this.createCommandBlock(commandBlock.get());
        }

        public void createAnvil(Supplier<? extends Block> anvilBlock) {
            this.createAnvil(anvilBlock.get());
        }
        
        public void createBeeNest(Supplier<? extends Block> beeNestBlock, Function<Block, TextureMapping> textureMappingGetter) {
            this.createBeeNest(beeNestBlock.get(), textureMappingGetter);
        }

        public void createCropBlock(Supplier<? extends Block> cropBlock, Property<Integer> ageProperty, int... ageToVisualStageMapping) {
            this.createCropBlock(cropBlock.get(), ageProperty, ageToVisualStageMapping);
        }
        
        public void createFurnace(Supplier<? extends Block> furnaceBlock, TexturedModel.Provider modelProvider) {
            this.createFurnace(furnaceBlock.get(), modelProvider);
        }

        @SafeVarargs
        public final void createCampfires(Supplier<? extends Block>... campfireBlocks) {
            this.createCampfires(Arrays.stream(campfireBlocks).map(Supplier::get).toArray(Block[]::new));
        }

        public void createAzalea(Supplier<? extends Block> azaleaBlock) {
            this.createAzalea(azaleaBlock.get());
        }

        public void createPottedAzalea(Supplier<? extends Block> pottedAzaleaBlock) {
            this.createPottedAzalea(pottedAzaleaBlock.get());
        }

        public void createMushroomBlock(Supplier<? extends Block> mushroomBlock) {
            this.createMushroomBlock(mushroomBlock.get());
        }
        
        public void createGenericCube(Supplier<? extends Block> block) {
            this.createGenericCube(block.get());
        }

        public void createPumpkinVariant(Supplier<? extends Block> pumpkinBlock, TextureMapping columnTextureMapping) {
            this.createPumpkinVariant(pumpkinBlock.get(), columnTextureMapping);
        }

        public void createDispenserBlock(Supplier<? extends Block> dispenserBlock) {
            this.createDispenserBlock(dispenserBlock.get());
        }

        public void createCopperBulb(Supplier<? extends Block> bulbBlock) {
            this.createCopperBulb(bulbBlock.get());
        }

        public BlockStateGenerator createCopperBulb(Supplier<? extends Block> bulbBlock, ResourceLocation unlit, ResourceLocation unlitPowered, ResourceLocation lit, ResourceLocation litPowered) {
            return this.createCopperBulb(bulbBlock.get(), unlit, unlitPowered, lit, litPowered);
        }

        public void copyCopperBulbModel(Supplier<? extends Block> bulbBlock, Supplier<? extends Block> sourceBlock) {
            this.copyCopperBulbModel(bulbBlock.get(), sourceBlock.get());
        }

        public void copyCopperBulbModel(Block bulbBlock, Supplier<? extends Block> sourceBlock) {
            this.copyCopperBulbModel(bulbBlock, sourceBlock.get());
        }

        public void copyCopperBulbModel(Supplier<? extends Block> bulbBlock, Block sourceBlock) {
            this.copyCopperBulbModel(bulbBlock.get(), sourceBlock);
        }

        public void createAmethystCluster(Supplier<? extends Block> amethystBlock) {
            this.createAmethystCluster(amethystBlock.get());
        }

        public void createNyliumBlock(Supplier<? extends Block> nyliumBlock) {
            this.createNyliumBlock(nyliumBlock.get());
        }

        public void createRotatableColumn(Supplier<? extends Block> rotatableColumnBlock) {
            this.createRotatableColumn(rotatableColumnBlock.get());
        }

        public List<ResourceLocation> createFloorFireModels(Supplier<? extends Block> fireBlock) {
            return this.createFloorFireModels(fireBlock.get());
        }

        public List<ResourceLocation> createSideFireModels(Supplier<? extends Block> fireBlock) {
            return this.createSideFireModels(fireBlock.get());
        }

        public List<ResourceLocation> createTopFireModels(Supplier<? extends Block> fireBlock) {
            return this.createTopFireModels(fireBlock.get());
        }

        public void createLantern(Supplier<? extends Block> lanternBlock) {
            this.createLantern(lanternBlock.get());
        }

        public void createGrassLikeBlock(Supplier<? extends Block> grassLikeBlock, ResourceLocation modelLocation, Variant variant) {
            this.createGrassLikeBlock(grassLikeBlock.get(), modelLocation, variant);
        }

        public void createWeightedPressurePlate(Supplier<? extends Block> pressurePlateBlock, Supplier<? extends Block> plateMaterialBlock) {
            this.createWeightedPressurePlate(pressurePlateBlock.get(), plateMaterialBlock.get());
        }

        public void createWeightedPressurePlate(Block pressurePlateBlock, Supplier<? extends Block> plateMaterialBlock) {
            this.createWeightedPressurePlate(pressurePlateBlock, plateMaterialBlock.get());
        }

        public void createWeightedPressurePlate(Supplier<? extends Block> pressurePlateBlock, Block plateMaterialBlock) {
            this.createWeightedPressurePlate(pressurePlateBlock.get(), plateMaterialBlock);
        }
        
        public void copyModel(Supplier<? extends Block> sourceBlock, Supplier<? extends Block> targetBlock) {
            this.copyModel(sourceBlock.get(), targetBlock.get());
        }

        public void copyModel(Block sourceBlock, Supplier<? extends Block> targetBlock) {
            this.copyModel(sourceBlock, targetBlock.get());
        }

        public void copyModel(Supplier<? extends Block> sourceBlock, Block targetBlock) {
            this.copyModel(sourceBlock.get(), targetBlock);
        }

        public void createNonTemplateHorizontalBlock(Supplier<? extends Block> horizontalBlock) {
            this.createNonTemplateModelBlock(horizontalBlock.get());
        }
        
        public void createPistonVariant(Supplier<? extends Block> pistonBlock, ResourceLocation baseModelLocation, TextureMapping topTextureMapping) {
            this.createPistonVariant(pistonBlock.get(), baseModelLocation, topTextureMapping);
        }

        public void createMultiface(Supplier<? extends Block> multifaceBlock) {
            this.createMultiface(multifaceBlock.get());
        }

        public void createShulkerBox(Supplier<? extends Block> shulkerBoxBlock) {
            this.createShulkerBox(shulkerBoxBlock.get());
        }

        public void createGrowingPlant(Supplier<? extends Block> growingPlantBlock, Supplier<? extends Block> plantBlock, TintState tintState) {
            this.createGrowingPlant(growingPlantBlock.get(), plantBlock.get(), tintState);
        }

        public void createGrowingPlant(Block growingPlantBlock, Supplier<? extends Block> plantBlock, TintState tintState) {
            this.createGrowingPlant(growingPlantBlock, plantBlock.get(), tintState);
        }

        public void createGrowingPlant(Supplier<? extends Block> growingPlantBlock, Block plantBlock, TintState tintState) {
            this.createGrowingPlant(growingPlantBlock.get(), plantBlock, tintState);
        }

        public void createBedItem(Supplier<? extends Block> bedBlock, Supplier<? extends Block> woolBlock) {
            this.createBedItem(bedBlock.get(), woolBlock.get());
        }

        public void createBedItem(Block bedBlock, Supplier<? extends Block> woolBlock) {
            this.createBedItem(bedBlock, woolBlock.get());
        }

        public void createBedItem(Supplier<? extends Block> bedBlock, Block woolBlock) {
            this.createBedItem(bedBlock.get(), woolBlock);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class AmbleBlockFamilyProvider {

        private final BlockModelGenerators.BlockFamilyProvider provider;

        public AmbleBlockFamilyProvider(BlockModelGenerators.BlockFamilyProvider provider) {
            this.provider = provider;
        }

        public AmbleBlockFamilyProvider fullBlock(Block block, ModelTemplate modelTemplate) {
            this.provider.fullBlock(block, modelTemplate);
            return this;
        }

        public AmbleBlockFamilyProvider fullBlock(Supplier<Block> block, ModelTemplate modelTemplate) {
            return this.fullBlock(block.get(), modelTemplate);
        }

        public AmbleBlockFamilyProvider donateModelTo(Block sourceBlock, Block block) {
            this.provider.donateModelTo(sourceBlock, block);
            return this;
        }

        public AmbleBlockFamilyProvider donateModelTo(Supplier<Block> sourceBlock, Supplier<Block> block) {
            return this.donateModelTo(sourceBlock.get(), block.get());
        }

        public AmbleBlockFamilyProvider donateModelTo(Block sourceBlock, Supplier<Block> block) {
            return this.donateModelTo(sourceBlock, block.get());
        }

        public AmbleBlockFamilyProvider donateModelTo(Supplier<Block> sourceBlock, Block block) {
            return this.donateModelTo(sourceBlock.get(), block);
        }

        public AmbleBlockFamilyProvider button(Block buttonBlock) {
            this.provider.button(buttonBlock);
            return this;
        }

        public AmbleBlockFamilyProvider button(Supplier<Block> buttonBlock) {
            return this.button(buttonBlock.get());
        }

        public AmbleBlockFamilyProvider wall(Block wallBlock) {
            this.provider.wall(wallBlock);
            return this;
        }

        public AmbleBlockFamilyProvider wall(Supplier<Block> wallBlock) {
            return this.wall(wallBlock.get());
        }

        public AmbleBlockFamilyProvider customFence(Block fenceBlock) {
            this.provider.customFence(fenceBlock);
            return this;
        }

        public AmbleBlockFamilyProvider customFence(Supplier<Block> fenceBlock) {
            return this.customFence(fenceBlock.get());
        }

        public AmbleBlockFamilyProvider fence(Block fenceBlock) {
            this.provider.fence(fenceBlock);
            return this;
        }

        public AmbleBlockFamilyProvider fence(Supplier<Block> fenceBlock) {
            return this.fence(fenceBlock.get());
        }

        public AmbleBlockFamilyProvider customFenceGate(Block customFenceGateBlock) {
            this.provider.customFenceGate(customFenceGateBlock);
            return this;
        }

        public AmbleBlockFamilyProvider customFenceGate(Supplier<Block> customFenceGateBlock) {
            return this.customFenceGate(customFenceGateBlock.get());
        }

        public AmbleBlockFamilyProvider fenceGate(Block fenceGateBlock) {
            this.provider.fenceGate(fenceGateBlock);
            return this;
        }

        public AmbleBlockFamilyProvider fenceGate(Supplier<Block> fenceGateBlock) {
            return this.fenceGate(fenceGateBlock.get());
        }

        public AmbleBlockFamilyProvider pressurePlate(Block pressurePlateBlock) {
            this.provider.pressurePlate(pressurePlateBlock);
            return this;
        }

        public AmbleBlockFamilyProvider pressurePlate(Supplier<Block> pressurePlateBlock) {
            return this.pressurePlate(pressurePlateBlock.get());
        }

        public AmbleBlockFamilyProvider sign(Block signBlock) {
            this.provider.sign(signBlock);
            return this;
        }

        public AmbleBlockFamilyProvider sign(Supplier<Block> signBlock) {
            return this.sign(signBlock.get());
        }

        public AmbleBlockFamilyProvider slab(Block slabBlock) {
            this.provider.slab(slabBlock);
            return this;
        }

        public AmbleBlockFamilyProvider slab(Supplier<Block> slabBlock) {
            return this.slab(slabBlock.get());
        }

        public AmbleBlockFamilyProvider stairs(Block stairsBlock) {
            this.provider.stairs(stairsBlock);
            return this;
        }

        public AmbleBlockFamilyProvider stairs(Supplier<Block> stairsBlock) {
            return this.stairs(stairsBlock.get());
        }

        public AmbleBlockFamilyProvider generateFor(BlockFamily family) {
            this.provider.generateFor(family);
            return this;
        }
    }
}
