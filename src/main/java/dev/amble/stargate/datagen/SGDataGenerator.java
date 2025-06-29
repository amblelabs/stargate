package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.lang.LanguageType;
import dev.amble.lib.datagen.lang.AmbleLanguageProvider;
import dev.amble.lib.datagen.loot.AmbleBlockLootTable;
import dev.amble.lib.datagen.model.AmbleModelProvider;
import dev.amble.lib.datagen.sound.AmbleSoundProvider;
import dev.amble.lib.datagen.tag.AmbleBlockTagProvider;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateItems;
import dev.amble.stargate.core.fluid.StargateFluids;
import dev.amble.stargate.world.StargateConfiguredFeature;
import dev.amble.stargate.world.StargatePlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static net.minecraft.data.server.recipe.RecipeProvider.*;

public class SGDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        genSounds(pack);
        genTags(pack);
        genLoot(pack);
        genModels(pack);
        generateRecipes(pack);
        pack.addProvider(StargateWorldGenerator::new);
       }

    private void genModels(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleModelProvider provider = new AmbleModelProvider(output);

            provider.withBlocks(StargateBlocks.class);
            provider.withItems(StargateItems.class);

            return provider;
        })));
    }


    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, StargateConfiguredFeature::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, StargatePlacedFeatures::bootstrap);
    }

    private void genTags(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> new AmbleBlockTagProvider(output, registriesFuture).withBlocks(StargateBlocks.class))));
    }
    private void genLoot(FabricDataGenerator.Pack pack) {
         pack.addProvider((((output, registriesFuture) -> new AmbleBlockLootTable(output).withBlocks(StargateBlocks.class))));
    }

    public void generateRecipes(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            StargateRecipeProvider provider = new StargateRecipeProvider(output);

            provider.addShapelessRecipe (ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.ADDRESS_CARTOUCHE, 1)
                    .input(Items.PAPER)
                    .input(Items.ENDER_PEARL)
                    .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                    .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, StargateItems.IRIS_BLADE, 1)
                    .pattern("II ")
                    .pattern("  N")
                    .pattern("   ")
                    .input('I', StargateItems.NAQUADAH_INGOT)
                    .input('N', StargateItems.NAQUADAH_NUGGET)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                    .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, StargateItems.IRIS_FRAME, 1)
                    .pattern("NIN")
                    .pattern("I I")
                    .pattern("NIN")
                    .input('N', Items.IRON_NUGGET)
                    .input('I', Items.IRON_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                    .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, StargateItems.IRIS, 1)
                    .pattern("BBB")
                    .pattern("BFB")
                    .pattern("BBB")
                    .input('F', StargateItems.IRIS_FRAME)
                    .input('B', StargateItems.IRIS_BLADE)
                    .criterion(hasItem(StargateItems.IRIS_FRAME), conditionsFromItem(StargateItems.IRIS_FRAME))
                    .criterion(hasItem(StargateItems.IRIS_BLADE), conditionsFromItem(StargateItems.IRIS_BLADE)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.NAQUADAH_BLOCK, 1)
                    .pattern("III")
                    .pattern("III")
                    .pattern("III")
                    .input('I', StargateItems.NAQUADAH_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));


            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 1)
                            .pattern("NNN")
                            .pattern("NNN")
                            .pattern("NNN")
                            .input('N', StargateItems.NAQUADAH_NUGGET)
                            .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)),
                    new Identifier("stargate", "naquadah_ingot_recipe_from_nugget")
            );

            provider.addShapelessRecipe(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 9)
                            .input(StargateBlocks.NAQUADAH_BLOCK)
                            .criterion(hasItem(StargateBlocks.NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.NAQUADAH_BLOCK)),
                    new Identifier("stargate", "naquadah_ingot_recipe_from_block")
            );

            provider.addBlastFurnaceRecipe(CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(StargateItems.RAW_NAQUADAH),
                            RecipeCategory.MISC,StargateItems.NAQUADAH_INGOT, 0.2f, 500)
                    .criterion(hasItem(StargateItems.RAW_NAQUADAH), conditionsFromItem(StargateItems.RAW_NAQUADAH)));

            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_NUGGET,9)
                    .input(StargateItems.NAQUADAH_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));

            return provider;
        })));
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleLanguageProvider provider = new AmbleLanguageProvider(output, LanguageType.EN_US);

            provider.translateBlocks(StargateBlocks.class);
            provider.addTranslation(StargateBlocks.DHD, "Dial-Home Device");
            provider.addTranslation(StargateBlocks.STARGATE, "Stargate");
            provider.addTranslation(StargateBlocks.NAQUADAH_BLOCK, "Block of Naquadah");
            provider.addTranslation(StargateBlocks.NAQUADAH_ORE, "Naquadah Ore");

            provider.translateItems(StargateItems.class);
            provider.addTranslation(StargateItems.ADDRESS_CARTOUCHE, "Address Cartouche");
            provider.addTranslation(StargateItems.IRIS_BLADE, "Iris Blade");
            provider.addTranslation(StargateItems.IRIS_FRAME, "Iris Frame");
            provider.addTranslation(StargateItems.IRIS, "Iris");
            provider.addTranslation(StargateItems.RAW_NAQUADAH, "Raw Naquadah");
            provider.addTranslation(StargateItems.NAQUADAH_INGOT, "Naquadah Ingot");
            provider.addTranslation(StargateItems.NAQUADAH_NUGGET, "Naquadah Nugget");
            provider.addTranslation(StargateFluids.LIQUID_NAQUADAH, "Liquid Naquadah Container");
            provider.addTranslation(StargateItems.EMPTY_CONTAINER, "Empty Container");

            provider.addTranslation("itemGroup.stargate.item_group", "STARGATE");

            provider.addTranslation("tooltip.stargate.link_item.holdformoreinfo", "Hold shift for more info");
            provider.addTranslation("tooltip.stargate.dialer.hint", "Use on stargate to link, then use on another stargate to dial");

            return provider;
        })));
    }

    private void genSounds(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleSoundProvider provider = new AmbleSoundProvider(output);

            return provider;
        })));
    }


}