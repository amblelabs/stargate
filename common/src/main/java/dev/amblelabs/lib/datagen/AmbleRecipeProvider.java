package dev.amblelabs.lib.datagen;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class AmbleRecipeProvider extends RecipeProvider {

    protected final String modId;

    protected AmbleRecipeProvider(PackOutput out, CompletableFuture<HolderLookup.Provider> future, String modId) {
        super(out, future);
        this.modId = modId;
    }

    protected ShapedRecipeBuilder ring(RecipeCategory category, ItemLike out, int count, Ingredient outer, @Nullable Ingredient inner) {
        return ringCornered(category, out, count, outer, outer, inner);
    }

    protected ShapedRecipeBuilder ring(RecipeCategory category, ItemLike out, int count, ItemLike outer, @Nullable ItemLike inner) {
        return ring(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ring(RecipeCategory category, ItemLike out, int count, TagKey<Item> outer, @Nullable TagKey<Item> inner) {
        return ring(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringCornerless(RecipeCategory category, ItemLike out, int count, Ingredient outer,
        @Nullable Ingredient inner) {
        return ringCornered(category, out, count, outer, null, inner);
    }

    protected ShapedRecipeBuilder ringCornerless(RecipeCategory category, ItemLike out, int count, ItemLike outer, @Nullable ItemLike inner) {
        return ringCornerless(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringCornerless(RecipeCategory category, ItemLike out, int count, TagKey<Item> outer,
        @Nullable TagKey<Item> inner) {
        return ringCornerless(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringAll(RecipeCategory category, ItemLike out, int count, Ingredient outer, @Nullable Ingredient inner) {
        return ringCornered(category, out, count, outer, outer, inner);
    }

    protected ShapedRecipeBuilder ringAll(RecipeCategory category, ItemLike out, int count, ItemLike outer, @Nullable ItemLike inner) {
        return ringAll(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringAll(RecipeCategory category, ItemLike out, int count, TagKey<Item> outer, @Nullable TagKey<Item> inner) {
        return ringAll(category, out, count, Ingredient.of(outer), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringCornered(RecipeCategory category, ItemLike out, int count, @Nullable Ingredient cardinal,
                                               @Nullable Ingredient diagonal, @Nullable Ingredient inner) {
        if (cardinal == null && diagonal == null && inner == null) {
            throw new IllegalArgumentException("at least one ingredient must be non-null");
        }
        if (inner != null && cardinal == null && diagonal == null) {
            throw new IllegalArgumentException("if inner is non-null, either cardinal or diagonal must not be");
        }

        var builder = ShapedRecipeBuilder.shaped(category, out, count);
        char C = ' ';
        if (cardinal != null) {
            builder.define('C', cardinal);
            C = 'C';
        }
        char D = ' ';
        if (diagonal != null) {
            builder.define('D', diagonal);
            D = 'D';
        }
        char I = ' ';
        if (inner != null) {
            builder.define('I', inner);
            I = 'I';
        }

        builder
            .pattern("" + D + C + D)
            .pattern("" + C + I + C)
            .pattern("" + D + C + D);

        return builder;
    }

    protected ShapedRecipeBuilder ringCornered(RecipeCategory category, ItemLike out, int count, @Nullable ItemLike cardinal,
        @Nullable ItemLike diagonal, @Nullable ItemLike inner) {
        return ringCornered(category, out, count, ingredientOf(cardinal), ingredientOf(diagonal), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder ringCornered(RecipeCategory category, ItemLike out, int count, @Nullable TagKey<Item> cardinal,
        @Nullable TagKey<Item> diagonal, @Nullable TagKey<Item> inner) {
        return ringCornered(category, out, count, ingredientOf(cardinal), ingredientOf(diagonal), ingredientOf(inner));
    }

    protected ShapedRecipeBuilder stack(RecipeCategory category, ItemLike out, int count, Ingredient top, Ingredient bottom) {
        return ShapedRecipeBuilder.shaped(category, out, count)
            .define('T', top)
            .define('B', bottom)
            .pattern("T")
            .pattern("B");
    }

    protected ShapedRecipeBuilder stack(RecipeCategory category, ItemLike out, int count, ItemLike top, ItemLike bottom) {
        return stack(category, out, count, Ingredient.of(top), Ingredient.of(bottom));
    }

    protected ShapedRecipeBuilder stack(RecipeCategory category, ItemLike out, int count, TagKey<Item> top, TagKey<Item> bottom) {
        return stack(category, out, count, Ingredient.of(top), Ingredient.of(bottom));
    }

    protected ShapedRecipeBuilder stick(RecipeCategory category, ItemLike out, int count, Ingredient input) {
        return stack(category, out, count, input, input);
    }

    protected ShapedRecipeBuilder stick(RecipeCategory category, ItemLike out, int count, ItemLike input) {
        return stick(category, out, count, Ingredient.of(input));
    }

    protected ShapedRecipeBuilder stick(RecipeCategory category, ItemLike out, int count, TagKey<Item> input) {
        return stick(category, out, count, Ingredient.of(input));
    }

    /**
     * @param largeSize True for a 3x3, false for a 2x2
     */
    protected void packing(RecipeCategory category, ItemLike free, ItemLike compressed, String freeName, boolean largeSize, RecipeOutput recipes) {
        var pack = ShapedRecipeBuilder.shaped(category, compressed)
            .define('X', free);

        if (largeSize) {
            pack.pattern("XXX").pattern("XXX").pattern("XXX");
        } else {
            pack.pattern("XX").pattern("XX");
        }

        pack.unlockedBy("has_item", hasItem(free)).save(recipes, modLoc(freeName + "_packing"));

        ShapelessRecipeBuilder.shapeless(category, free, largeSize ? 9 : 4)
            .requires(compressed)
            .unlockedBy("has_item", hasItem(free)).save(recipes, modLoc(freeName + "_unpacking"));
    }

    protected ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    @Nullable
    protected Ingredient ingredientOf(@Nullable ItemLike item) {
        return item == null ? null : Ingredient.of(item);
    }

    @Nullable
    protected Ingredient ingredientOf(@Nullable TagKey<Item> item) {
        return item == null ? null : Ingredient.of(item);
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(MinMaxBounds.Ints bounds, ItemLike itemLike) {
        return ambleInventoryTrigger(ItemPredicate.Builder.item().of(itemLike).withCount(bounds).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike itemLike) {
        return ambleInventoryTrigger(ItemPredicate.Builder.item().of(itemLike).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(TagKey<Item> itemTag) {
        return ambleInventoryTrigger(ItemPredicate.Builder.item().of(itemTag).build());
    }

    protected static Criterion<InventoryChangeTrigger.TriggerInstance> ambleInventoryTrigger(ItemPredicate... predicates) {
        return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates)));
    }
}