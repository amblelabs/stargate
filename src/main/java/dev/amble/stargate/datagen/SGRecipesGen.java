package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.recipe.AmbleRecipeProvider;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class SGRecipesGen extends AmbleRecipeProvider {

    public SGRecipesGen(FabricDataOutput output) {
        super(output);

        addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.ADDRESS_CARTOUCHE, 1)
                .input(Items.PAPER)
                .input(Items.ENDER_PEARL)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL)));

        addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS_BLADE, 1)
                .group("iris")
                .pattern("II ")
                .pattern("  N")
                .pattern("   ")
                .input('I', StargateItems.NAQUADAH_INGOT)
                .input('N', StargateItems.NAQUADAH_NUGGET)
                .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

        addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS_FRAME, 1)
                .group("iris")
                .pattern("NIN")
                .pattern("I I")
                .pattern("NIN")
                .input('N', Items.IRON_NUGGET)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

        addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS, 1)
                .group("iris")
                .pattern("BBB")
                .pattern("BFB")
                .pattern("BBB")
                .input('F', StargateItems.IRIS_FRAME)
                .input('B', StargateItems.IRIS_BLADE)
                .criterion(hasItem(StargateItems.IRIS_FRAME), conditionsFromItem(StargateItems.IRIS_FRAME))
                .criterion(hasItem(StargateItems.IRIS_BLADE), conditionsFromItem(StargateItems.IRIS_BLADE)));

        addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateBlocks.NAQUADAH_BLOCK, 1)
                .group("naquadah")
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .input('I', StargateItems.NAQUADAH_INGOT)
                .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));

        addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateBlocks.RAW_NAQUADAH_BLOCK, 1)
                .group("naquadah")
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .input('R', StargateItems.RAW_NAQUADAH)
                .criterion(hasItem(StargateItems.RAW_NAQUADAH), conditionsFromItem(StargateItems.RAW_NAQUADAH)));


        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 1)
                        .group("naquadah")
                        .pattern("NNN")
                        .pattern("NNN")
                        .pattern("NNN")
                        .input('N', StargateItems.NAQUADAH_NUGGET)
                        .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)),
                new Identifier("stargate", "naquadah_ingot_recipe_from_nugget")
        );

        addShapelessRecipe(
                ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 9)
                        .group("naquadah")
                        .input(StargateBlocks.NAQUADAH_BLOCK)
                        .criterion(hasItem(StargateBlocks.NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.NAQUADAH_BLOCK)),
                new Identifier("stargate", "naquadah_ingot_recipe_from_block")
        );

        addShapelessRecipe(
                ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.RAW_NAQUADAH, 9)
                        .group("naquadah")
                        .input(StargateBlocks.RAW_NAQUADAH_BLOCK)
                        .criterion(hasItem(StargateBlocks.RAW_NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.RAW_NAQUADAH_BLOCK)),
                new Identifier("stargate", "raw_naquadah_recipe_from_block")
        );

        addBlastFurnaceRecipe(CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(StargateItems.RAW_NAQUADAH),
                        RecipeCategory.MISC,StargateItems.NAQUADAH_INGOT, 0.2f, 500)
                .criterion(hasItem(StargateItems.RAW_NAQUADAH), conditionsFromItem(StargateItems.RAW_NAQUADAH)));

        addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_NUGGET,9)
                .group("naquadah")
                .input(StargateItems.NAQUADAH_INGOT)
                .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, StargateItems.COPPER_COIL, 8)
                        .pattern(" C ")
                        .pattern("CWC")
                        .pattern(" C ")
                        .input('C', Items.COPPER_INGOT)
                        .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                        .input('W', ItemTags.PLANKS));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.EMPTY_CONTAINER, 1)
                        .pattern("GIG")
                        .pattern("G G")
                        .pattern(" G ")
                        .input('G', Blocks.GLASS)
                        .input('I', Items.IRON_INGOT)
                        .criterion(hasItem(Blocks.GLASS), conditionsFromItem(Blocks.GLASS))
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, StargateBlocks.TOASTER)
                        .pattern("ICI")
                        .pattern("ICI")
                        .pattern("SRS")
                        .input('I', Items.IRON_INGOT)
                        .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                        .input('C', StargateItems.COPPER_COIL)
                        .criterion(hasItem(StargateItems.COPPER_COIL), conditionsFromItem(StargateItems.COPPER_COIL))
                        .input('S', Items.DRIED_KELP)
                        .criterion(hasItem(Items.DRIED_KELP), conditionsFromItem(Items.DRIED_KELP))
                        .input('R', Items.REDSTONE)
                        .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.CONTROL_CRYSTAL_RED, 1)
                        .group("crystal")
                        .pattern("DAD")
                        .pattern("RAR")
                        .pattern(" R ")
                        .input('A', Items.AMETHYST_SHARD)
                        .input('R', Items.REDSTONE)
                        .input('D', Items.RED_DYE)
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                        .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.CONTROL_CRYSTAL_YELLOW, 1)
                        .group("crystal")
                        .pattern("DAD")
                        .pattern("RAR")
                        .pattern(" R ")
                        .input('A', Items.AMETHYST_SHARD)
                        .input('R', Items.REDSTONE)
                        .input('D', Items.YELLOW_DYE)
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                        .criterion(hasItem(Items.YELLOW_DYE), conditionsFromItem(Items.YELLOW_DYE)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.CONTROL_CRYSTAL_BLUE, 1)
                        .group("crystal")
                        .pattern("DAD")
                        .pattern("RAR")
                        .pattern(" R ")
                        .input('A', Items.AMETHYST_SHARD)
                        .input('R', Items.REDSTONE)
                        .input('D', Items.BLUE_DYE)
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                        .criterion(hasItem(Items.BLUE_DYE), conditionsFromItem(Items.BLUE_DYE)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.CONTROL_CRYSTAL_MASTER, 1)
                        .group("crystal")
                        .pattern("DAD")
                        .pattern("RAR")
                        .pattern(" R ")
                        .input('A', Items.AMETHYST_SHARD)
                        .input('R', Blocks.REDSTONE_BLOCK)
                        .input('D', Items.RED_DYE)
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(Blocks.REDSTONE_BLOCK), conditionsFromItem(Blocks.REDSTONE_BLOCK))
                        .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE)));

        addShapedRecipe(
                ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, StargateBlocks.DHD, 1)
                        .pattern("NPN")
                        .pattern("BMY")
                        .pattern("NRN")
                        .input('N', StargateBlocks.NAQUADAH_BLOCK)
                        .input('R', StargateItems.CONTROL_CRYSTAL_RED)
                        .input('B', StargateItems.CONTROL_CRYSTAL_BLUE)
                        .input('Y', StargateItems.CONTROL_CRYSTAL_YELLOW)
                        .input('M', StargateItems.CONTROL_CRYSTAL_MASTER)
                        .input('P', Items.STONE_BUTTON)
                        .criterion(hasItem(StargateBlocks.NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.NAQUADAH_BLOCK))
                        .criterion(hasItem(StargateItems.CONTROL_CRYSTAL_RED), conditionsFromItem(StargateItems.CONTROL_CRYSTAL_RED))
                        .criterion(hasItem(StargateItems.CONTROL_CRYSTAL_BLUE), conditionsFromItem(StargateItems.CONTROL_CRYSTAL_BLUE))
                        .criterion(hasItem(StargateItems.CONTROL_CRYSTAL_YELLOW), conditionsFromItem(StargateItems.CONTROL_CRYSTAL_YELLOW))
                        .criterion(hasItem(StargateItems.CONTROL_CRYSTAL_MASTER), conditionsFromItem(StargateItems.CONTROL_CRYSTAL_MASTER))
                        .criterion(hasItem(Items.STONE_BUTTON), conditionsFromItem(Items.STONE_BUTTON)));
    }
}
