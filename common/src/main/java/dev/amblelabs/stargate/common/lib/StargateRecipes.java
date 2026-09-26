package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class StargateRecipes {

    private static final XplatRegistrar<RecipeSerializer<?>> SERIALIZERS_REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.RECIPE_SERIALIZER);
    private static final XplatRegistrar<RecipeType<?>> TYPES_REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.RECIPE_TYPE);

    public static void register() {
        SERIALIZERS_REGISTRAR.registerAll();
        TYPES_REGISTRAR.registerAll();
    }

    public static final Supplier<RecipeType<ToastingRecipe>> TOASTING = recipe("toasting", ToastingRecipe.SERIALIZER);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Recipe<?>> Supplier<RecipeType<T>> recipe(String name, RecipeSerializer<T> serializer) {
        SERIALIZERS_REGISTRAR.register(name, () -> serializer);

        return TYPES_REGISTRAR.register(name, () -> new RecipeType<>() {

            @Override
            public String toString() {
                return StargateAPI.MOD_ID + ":" + name;
            }
        });
    }
}
