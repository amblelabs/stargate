package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class StargateRecipes {

    private static final XplatRegister<RecipeSerializer<?>> REGISTER_SERIALIZERS = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.RECIPE_SERIALIZER);
    private static final XplatRegister<RecipeType<?>> REGISTER_TYPES = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.RECIPE_TYPE);

    public static void register() {
        REGISTER_SERIALIZERS.registerAll();
        REGISTER_TYPES.registerAll();
    }

    public static final Supplier<RecipeType<ToastingRecipe>> TOASTING = recipe("toasting", ToastingRecipe.SERIALIZER);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Recipe<?>> Supplier<RecipeType<T>> recipe(String name, RecipeSerializer<T> serializer) {
        REGISTER_SERIALIZERS.register(name, () -> serializer);

        return REGISTER_TYPES.register(name, () -> new RecipeType<T>() {

            @Override
            public String toString() {
                return StargateAPI.MOD_ID + ":" + name;
            }
        });
    }
}
