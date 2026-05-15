package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.recipe.ToastingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateRecipes {

    public static void registerSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
        for (var e : SERIALIZERS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static void registerTypes(BiConsumer<RecipeType<?>, ResourceLocation> r) {
        for (var e : TYPES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, RecipeSerializer<?>> SERIALIZERS = new LinkedHashMap<>();
    private static final Map<ResourceLocation, RecipeType<?>> TYPES = new LinkedHashMap<>();

    public static final RecipeType<ToastingRecipe> TOASTING = recipe("toasting", ToastingRecipe.SERIALIZER);

    @SuppressWarnings("SameParameterValue")
    private static <T extends net.minecraft.world.item.crafting.Recipe<?>> RecipeType<T> recipe(String name, RecipeSerializer<T> serializer) {
        var id = modLoc(name);

        var type = new RecipeType<T>(id) {

            @Override
            public String toString() {
                return id.toString();
            }
        };

        {
            var old = TYPES.put(id, type);

            if (old != null) {
                throw new IllegalArgumentException("Typo? Duplicate type id " + name);
            }
        }

        {
            var old = SERIALIZERS.put(id, serializer);

            if (old != null) {
                throw new IllegalArgumentException("Typo? Duplicate serializer id " + name);
            }
        }

        return type;
    }
}
