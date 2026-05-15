package dev.amblelabs.stargate.datagen.recipe;

import dev.amblelabs.lib.datagen.AmbleRecipeProvider;
import dev.amblelabs.stargate.api.StargateAPI;
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
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

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