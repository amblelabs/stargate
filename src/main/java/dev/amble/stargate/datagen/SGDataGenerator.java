package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.advancement.AmbleAdvancementProvider;
import dev.amble.lib.datagen.lang.AmbleLanguageProvider;
import dev.amble.lib.datagen.lang.LanguageType;
import dev.amble.lib.datagen.loot.AmbleBlockLootTable;
import dev.amble.lib.datagen.model.AmbleModelProvider;
import dev.amble.lib.datagen.recipe.AmbleRecipeProvider;
import dev.amble.lib.datagen.sound.AmbleSoundProvider;
import dev.amble.lib.datagen.tag.AmbleBlockTagProvider;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateCriterions;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.world.StargateConfiguredFeature;
import dev.amble.stargate.world.StargatePlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import static net.minecraft.data.server.recipe.RecipeProvider.conditionsFromItem;
import static net.minecraft.data.server.recipe.RecipeProvider.hasItem;

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
        generateAdvancements(pack);

        pack.addProvider(StargateWorldGenerator::new);
   }

    private void genModels(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) ->
                new AmbleModelProvider(output)
                        .withBlocks(StargateBlocks.class)
                        .withItems(StargateItems.class))));
    }

    private void generateAdvancements(FabricDataGenerator.Pack pack) {
        pack.addProvider((output, registriesFuture) -> {
            AmbleAdvancementProvider provider = new AmbleAdvancementProvider(output);

            Advancement root = provider.create("root").icon(StargateBlocks.GENERIC_GATE)
                    .noToast().silent().background("textures/block/raw_naquadah_block.png")
                    .condition("root", InventoryChangedCriterion.Conditions.items(
                            StargateItems.ORLIN_STARGATE, StargateItems.DESTINY_STARGATE,
                            StargateItems.MILKY_WAY_STARGATE, StargateItems.PEGASUS_STARGATE
                    )).build();

            Advancement rawNaquadah = provider.challenge(root, "obtain_raw_naquadah").icon(StargateItems.RAW_NAQUADAH)
                    .condition("obtain_raw_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.RAW_NAQUADAH))
                    .hidden().build();

            Advancement addressCartouche = provider.goal(root, "obtain_address_cartouche").icon(StargateItems.ADDRESS_CARTOUCHE)
                    .condition("obtain_address_cartouche", InventoryChangedCriterion.Conditions.items(StargateItems.ADDRESS_CARTOUCHE))
                    .hidden().build();

            Advancement liquidNaquadah = provider.goal(rawNaquadah, "obtain_liquid_naquadah").icon(StargateItems.LIQUID_NAQUADAH)
                    .condition("obtain_liquid_naquadah", InventoryChangedCriterion.Conditions.items(StargateItems.LIQUID_NAQUADAH))
                    .hidden().build();

            Advancement toaster = provider.goal(root, "obtain_toaster").icon(StargateBlocks.TOASTER)
                    .condition("obtain_toaster", InventoryChangedCriterion.Conditions.items(StargateBlocks.TOASTER))
                    .hidden().build();

            Advancement burntToast = provider.challenge(toaster, "obtain_burnt_toast").icon(StargateItems.BURNT_TOAST)
                    .condition("obtain_burnt_toast", InventoryChangedCriterion.Conditions.items(StargateItems.BURNT_TOAST))
                    .hidden().build();

            Advancement passedThrough = provider.challenge(root, "passed_through").icon(StargateItems.DESTINY_STARGATE)
                    .condition("has_passed_through", StargateCriterions.PASSED_THROUGH.conditions()).build();

            return provider;
        });
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
            AmbleRecipeProvider provider = new AmbleRecipeProvider(output);

            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.ADDRESS_CARTOUCHE, 1)
                    .input(Items.PAPER)
                    .input(Items.ENDER_PEARL)
                    .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                    .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS_BLADE, 1)
                    .group("iris")
                    .pattern("II ")
                    .pattern("  N")
                    .pattern("   ")
                    .input('I', StargateItems.NAQUADAH_INGOT)
                    .input('N', StargateItems.NAQUADAH_NUGGET)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                    .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS_FRAME, 1)
                    .group("iris")
                    .pattern("NIN")
                    .pattern("I I")
                    .pattern("NIN")
                    .input('N', Items.IRON_NUGGET)
                    .input('I', Items.IRON_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT))
                    .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.IRIS, 1)
                    .group("iris")
                    .pattern("BBB")
                    .pattern("BFB")
                    .pattern("BBB")
                    .input('F', StargateItems.IRIS_FRAME)
                    .input('B', StargateItems.IRIS_BLADE)
                    .criterion(hasItem(StargateItems.IRIS_FRAME), conditionsFromItem(StargateItems.IRIS_FRAME))
                    .criterion(hasItem(StargateItems.IRIS_BLADE), conditionsFromItem(StargateItems.IRIS_BLADE)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateBlocks.NAQUADAH_BLOCK, 1)
                    .group("naquadah")
                    .pattern("III")
                    .pattern("III")
                    .pattern("III")
                    .input('I', StargateItems.NAQUADAH_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateBlocks.RAW_NAQUADAH_BLOCK, 1)
                    .group("naquadah")
                    .pattern("RRR")
                    .pattern("RRR")
                    .pattern("RRR")
                    .input('R', StargateItems.RAW_NAQUADAH)
                    .criterion(hasItem(StargateItems.RAW_NAQUADAH), conditionsFromItem(StargateItems.RAW_NAQUADAH)));


            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 1)
                            .group("naquadah")
                            .pattern("NNN")
                            .pattern("NNN")
                            .pattern("NNN")
                            .input('N', StargateItems.NAQUADAH_NUGGET)
                            .criterion(hasItem(StargateItems.NAQUADAH_NUGGET), conditionsFromItem(StargateItems.NAQUADAH_NUGGET)),
                    new Identifier("stargate", "naquadah_ingot_recipe_from_nugget")
            );

            provider.addShapelessRecipe(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_INGOT, 9)
                            .group("naquadah")
                            .input(StargateBlocks.NAQUADAH_BLOCK)
                            .criterion(hasItem(StargateBlocks.NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.NAQUADAH_BLOCK)),
                    new Identifier("stargate", "naquadah_ingot_recipe_from_block")
            );

            provider.addShapelessRecipe(
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.RAW_NAQUADAH, 9)
                            .group("naquadah")
                            .input(StargateBlocks.RAW_NAQUADAH_BLOCK)
                            .criterion(hasItem(StargateBlocks.RAW_NAQUADAH_BLOCK), conditionsFromItem(StargateBlocks.RAW_NAQUADAH_BLOCK)),
                    new Identifier("stargate", "raw_naquadah_recipe_from_block")
            );

            provider.addBlastFurnaceRecipe(CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(StargateItems.RAW_NAQUADAH),
                            RecipeCategory.MISC,StargateItems.NAQUADAH_INGOT, 0.2f, 500)
                    .criterion(hasItem(StargateItems.RAW_NAQUADAH), conditionsFromItem(StargateItems.RAW_NAQUADAH)));

            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.NAQUADAH_NUGGET,9)
                    .group("naquadah")
                    .input(StargateItems.NAQUADAH_INGOT)
                    .criterion(hasItem(StargateItems.NAQUADAH_INGOT), conditionsFromItem(StargateItems.NAQUADAH_INGOT)));

            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, StargateItems.COPPER_COIL, 8)
                            .pattern(" C ")
                            .pattern("CWC")
                            .pattern(" C ")
                            .input('C', Items.COPPER_INGOT)
                            .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                            .input('W', ItemTags.PLANKS));

            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, StargateItems.EMPTY_CONTAINER, 1)
                            .pattern("GIG")
                            .pattern("G G")
                            .pattern(" G ")
                            .input('G', Blocks.GLASS)
                            .input('I', Items.IRON_INGOT)
                            .criterion(hasItem(Blocks.GLASS), conditionsFromItem(Blocks.GLASS))
                            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT)));

            provider.addShapedRecipe(
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

            provider.addShapedRecipe(
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

            provider.addShapedRecipe(
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

            provider.addShapedRecipe(
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

            provider.addShapedRecipe(
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

            provider.addShapedRecipe(
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

            return provider;
        })));
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleLanguageProvider provider = new AmbleLanguageProvider(output, LanguageType.EN_US);

            //Blocks
            provider.translateBlocks(StargateBlocks.class);
            provider.addTranslation(StargateBlocks.DHD, "Dial-Home Device");
            provider.addTranslation(StargateBlocks.GENERIC_GATE, "Stargate");
            provider.addTranslation(StargateBlocks.ORLIN_GATE, "Orlin Stargate");
            provider.addTranslation(StargateBlocks.NAQUADAH_BLOCK, "Block of Naquadah");
            provider.addTranslation(StargateBlocks.RAW_NAQUADAH_BLOCK, "Block of Raw Naquadah");
            provider.addTranslation(StargateBlocks.NAQUADAH_ORE, "Naquadah Ore");
            provider.addTranslation(StargateBlocks.TOASTER, "Toaster");

            //Items
            provider.translateItems(StargateItems.class);
            provider.addTranslation(StargateItems.ADDRESS_CARTOUCHE, "Address Cartouche");
            provider.addTranslation(StargateItems.IRIS_BLADE, "Iris Blade");
            provider.addTranslation(StargateItems.IRIS_FRAME, "Iris Frame");
            provider.addTranslation(StargateItems.IRIS, "Iris");
            provider.addTranslation(StargateItems.RAW_NAQUADAH, "Raw Naquadah");
            provider.addTranslation(StargateItems.NAQUADAH_INGOT, "Naquadah Ingot");
            provider.addTranslation(StargateItems.NAQUADAH_NUGGET, "Naquadah Nugget");
            provider.addTranslation(StargateItems.LIQUID_NAQUADAH, "Liquid Naquadah Container");
            provider.addTranslation(StargateItems.EMPTY_CONTAINER, "Empty Container");
            provider.addTranslation(StargateItems.COPPER_COIL, "Copper Wire");
            provider.addTranslation(StargateItems.CONTROL_CRYSTAL_BLUE, "Blue Control Crystal");
            provider.addTranslation(StargateItems.CONTROL_CRYSTAL_YELLOW, "Yellow Control Crystal");
            provider.addTranslation(StargateItems.CONTROL_CRYSTAL_RED, "Red Control Crystal");
            provider.addTranslation(StargateItems.CONTROL_CRYSTAL_MASTER, "Master Control Crystal");
            provider.addTranslation(StargateItems.TOAST, "Toast");
            provider.addTranslation(StargateItems.BURNT_TOAST, "Burnt Toast");

            // Stargate Items
            provider.addTranslation(StargateItems.MILKY_WAY_STARGATE, "Milky Way Stargate");
            provider.addTranslation(StargateItems.ORLIN_STARGATE, "Orlin Stargate");
            provider.addTranslation(StargateItems.PEGASUS_STARGATE, "Pegasus Stargate");
            provider.addTranslation(StargateItems.DESTINY_STARGATE, "Destiny Stargate");

            //Misc
            provider.addTranslation("itemGroup.stargate.item_group", "STARGATE");

            provider.addTranslation("tooltip.stargate.link_item.holdformoreinfo", "Hold shift for more info");
            provider.addTranslation("tooltip.stargate.dialer.hint", "Use on stargate to link, then use on another stargate to dial");

            provider.addTranslation("text.stargate.gate", "STARGATE");
            provider.addTranslation("text.stargate.linked", "Linked to: ");

            provider.addTranslation("death.attack.stargate_flow", "%s went against the flow");
            provider.addTranslation("attribute.stargate.spacial_resistance", "Spacial Resistance");
            provider.addTranslation("effect.stargate.spacial_dynamic", "Spacial Dynamic");

            // Commands
            provider.addTranslation("command.stargate.generic.missing", "No Stargate found!");
            provider.addTranslation("command.stargate.generic.unavailable", "This Stargate is not available!");

            //Achievements
            provider.addTranslation("achievement.stargate.title.root", "Stargate Mod");
            provider.addTranslation("achievement.stargate.description.root", "Begin your journey through the stars!");

            provider.addTranslation("achievement.stargate.title.obtain_raw_naquadah", "The Fifth Race");
            provider.addTranslation("achievement.stargate.description.obtain_raw_naquadah", "Discover the element the Stargates are made of.");

            provider.addTranslation("achievement.stargate.title.obtain_address_cartouche", "It was right in front of us!");
            provider.addTranslation("achievement.stargate.description.obtain_address_cartouche", "Create an Address Cartouche to dial the Stargate Address.");

            provider.addTranslation("achievement.stargate.title.obtain_liquid_naquadah", "Enough power to dial the Eighth Chevron...?!");
            provider.addTranslation("achievement.stargate.description.obtain_liquid_naquadah", "Create or find, then put Liquid Naquadah into a Container.");

            provider.addTranslation("achievement.stargate.title.obtain_toaster", "You'll need a new one after this!");
            provider.addTranslation("achievement.stargate.description.obtain_toaster", "Make a toaster for the Orlin gate.");

            provider.addTranslation("achievement.stargate.title.obtain_burnt_toast", "Bit too long in the toaster");
            provider.addTranslation("achievement.stargate.description.obtain_burnt_toast", "Obtain burnt toast!");

            return provider;
        })));
    }

    private void genSounds(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> new AmbleSoundProvider(output))));
    }
}