package dev.amblelabs.stargate.datagen.recipe;

import dev.amblelabs.lib.datagen.AmbleRecipeProvider;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.datagen.IXplatIngredients;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

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

    }

    private void stoneCutterFromTag(RecipeOutput recipes, TagKey<Item> tagKey, Item... results) {
        for (Item result : results) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(tagKey), RecipeCategory.BUILDING_BLOCKS, result)
                    .unlockedBy("has_item", hasItem(tagKey))
                    .save(recipes, modLoc("stonecutting/" + result));
        }
    }
}