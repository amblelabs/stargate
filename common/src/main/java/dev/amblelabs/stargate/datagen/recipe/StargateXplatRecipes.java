package dev.amblelabs.stargate.datagen.recipe;

import dev.amblelabs.lib.datagen.AmbleRecipeProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateItems;
import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import dev.amblelabs.stargate.datagen.IXplatIngredients;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.lang.reflect.Constructor;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class StargateXplatRecipes extends AmbleRecipeProvider {

    @SuppressWarnings("FieldCanBeLocal")
    private final IXplatIngredients ingredients;

    public StargateXplatRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> future, IXplatIngredients ingredients) {
        super(output, future, StargateAPI.MOD_ID);

        this.ingredients = ingredients;
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        foodToasting(recipeOutput, 10, Items.BREAD, StargateItems.TOAST, 1);
        foodToasting(recipeOutput, 20, StargateItems.TOAST, StargateItems.BURNT_TOAST, 1);

        // SANDSTONE RECIPES
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.SANDSTONE_BRICKS, 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', Blocks.CUT_SANDSTONE)
                .unlockedBy(getHasName(Blocks.CUT_SANDSTONE), has(Blocks.CUT_SANDSTONE))
                .save(recipeOutput);

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(Blocks.SANDSTONE),
                        RecipeCategory.BUILDING_BLOCKS,
                        StargateBlocks.SANDSTONE_BRICKS,
                        1
                )
                .unlockedBy(getHasName(Blocks.SANDSTONE), has(Blocks.SANDSTONE))
                .save(recipeOutput, modLoc("sandstone_bricks_from_stonecutter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.SANDSTONE_BRICK_STAIRS, 4)
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .define('S', StargateBlocks.SANDSTONE_BRICKS)
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput);

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(StargateBlocks.SANDSTONE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        StargateBlocks.SANDSTONE_BRICK_STAIRS,
                        1
                )
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput, modLoc("sandstone_brick_stairs_from_stonecutter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.SANDSTONE_BRICK_SLAB, 6)
                .pattern("SSS")
                .define('S', StargateBlocks.SANDSTONE_BRICKS)
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput);

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(StargateBlocks.SANDSTONE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        StargateBlocks.SANDSTONE_BRICK_SLAB,
                        2
                )
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput, modLoc("sandstone_brick_slab_from_stonecutter"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.SANDSTONE_BRICK_WALL, 6)
                .pattern("SSS")
                .pattern("SSS")
                .define('S', StargateBlocks.SANDSTONE_BRICKS)
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput);

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(StargateBlocks.SANDSTONE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        StargateBlocks.SANDSTONE_BRICK_WALL,
                        1
                )
                .unlockedBy(getHasName(StargateBlocks.SANDSTONE_BRICKS), has(StargateBlocks.SANDSTONE_BRICKS))
                .save(recipeOutput, modLoc("sandstone_brick_wall_from_stonecutter"));

        // NAQUADAH RECIPES
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.NAQUADAH_BLOCK, 1)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', StargateItems.NAQUADAH_INGOT)
                .unlockedBy(getHasName(StargateItems.NAQUADAH_INGOT), has(StargateItems.NAQUADAH_INGOT))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 9)
                .requires(StargateBlocks.NAQUADAH_BLOCK)
                .unlockedBy(getHasName(StargateBlocks.NAQUADAH_BLOCK), has(StargateBlocks.NAQUADAH_BLOCK))
                .save(recipeOutput, modLoc("naquadah_ingot_from_naquadah_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, StargateBlocks.RAW_NAQUADAH_BLOCK, 1)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', StargateItems.RAW_NAQUADAH)
                .unlockedBy(getHasName(StargateItems.RAW_NAQUADAH), has(StargateItems.RAW_NAQUADAH))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, StargateItems.RAW_NAQUADAH, 9)
                .requires(StargateBlocks.RAW_NAQUADAH_BLOCK)
                .unlockedBy(getHasName(StargateBlocks.RAW_NAQUADAH_BLOCK), has(StargateBlocks.RAW_NAQUADAH_BLOCK))
                .save(recipeOutput, modLoc("raw_naquadah_from_raw_naquadah_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 1)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', StargateItems.NAQUADAH_NUGGET)
                .unlockedBy(getHasName(StargateItems.NAQUADAH_NUGGET), has(StargateItems.NAQUADAH_NUGGET))
                .save(recipeOutput, modLoc("naquadah_ingot_from_nuggets"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, StargateItems.NAQUADAH_NUGGET, 9)
                .requires(StargateItems.NAQUADAH_INGOT)
                .unlockedBy(getHasName(StargateItems.NAQUADAH_INGOT), has(StargateItems.NAQUADAH_INGOT))
                .save(recipeOutput);

       SimpleCookingRecipeBuilder.blasting(
               Ingredient.of(StargateBlocks.NAQUADAH_ORE),
               RecipeCategory.MISC,
               StargateItems.NAQUADAH_INGOT,
               0.7f,
               100)
               .unlockedBy(getHasName(StargateBlocks.NAQUADAH_ORE), has(StargateBlocks.NAQUADAH_ORE))
               .save(recipeOutput, modLoc("naquadah_ingot_from_blasting_naquadah_ore"));

        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(StargateItems.RAW_NAQUADAH),
                RecipeCategory.MISC,
                StargateItems.NAQUADAH_INGOT,
                0.7f,
                100)
                .unlockedBy(getHasName(StargateItems.RAW_NAQUADAH), has(StargateItems.RAW_NAQUADAH))
                .save(recipeOutput, modLoc("naquadah_ingot_from_blasting_raw_naquadah"));

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(StargateBlocks.NAQUADAH_ORE),
                RecipeCategory.MISC,
                StargateItems.NAQUADAH_INGOT,
                0.7f,
                100)
                .unlockedBy(getHasName(StargateBlocks.NAQUADAH_ORE), has(StargateBlocks.NAQUADAH_ORE))
                .save(recipeOutput, modLoc("naquadah_ingot_from_smelting_naquadah_ore"));

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(StargateItems.RAW_NAQUADAH),
                RecipeCategory.MISC,
                StargateItems.NAQUADAH_INGOT,
                0.7f,
                100)
                .unlockedBy(getHasName(StargateItems.RAW_NAQUADAH), has(StargateItems.RAW_NAQUADAH))
                .save(recipeOutput, modLoc("naquadah_ingot_from_smelting_raw_naquadah"));

    }

    public static void foodToasting(RecipeOutput recipeOutput, int cookingTime, ItemLike material, ItemLike result, float experience) {
        toasting(recipeOutput, RecipeCategory.FOOD, cookingTime, material, result, experience);
    }

    public static void toasting(RecipeOutput recipeOutput, RecipeCategory category, int cookingTime, ItemLike material, ItemLike result, float experience) {
        final String cookingMethod = "toasting";

        toasting(Ingredient.of(material), category, result, experience, cookingTime)
                .unlockedBy(getHasName(material), has(material))
                .save(recipeOutput, getItemName(result) + "_from_" + cookingMethod);
    }

    public static SimpleCookingRecipeBuilder toasting(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime) {
        AbstractCookingRecipe.Factory<?> factory = ToastingRecipe::new;

        try {
            Constructor<?> constructor = SimpleCookingRecipeBuilder.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

            return (SimpleCookingRecipeBuilder) constructor
                    .newInstance(category, CookingBookCategory.FOOD, result, ingredient, experience, cookingTime, factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void stoneCutterFromTag(RecipeOutput recipes, TagKey<Item> tagKey, Item... results) {
        for (Item result : results) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(tagKey), RecipeCategory.BUILDING_BLOCKS, result)
                    .unlockedBy("has_item", hasItem(tagKey))
                    .save(recipes, modLoc("stonecutting/" + result));
        }
    }
}