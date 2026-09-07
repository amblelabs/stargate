package dev.amblelabs.stargate.common.recipe;

import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.common.lib.StargateRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class ToastingRecipe extends AbstractCookingRecipe {

    public static final RecipeSerializer<ToastingRecipe> SERIALIZER = new SimpleCookingSerializer<>(ToastingRecipe::new, 20*3);

    public ToastingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(StargateRecipes.TOASTING.get(), group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(StargateBlocks.TOASTER.asItem());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
