package dev.amblelabs.stargate.fabric.datagen;

import dev.amblelabs.stargate.datagen.IXplatIngredients;
import dev.amblelabs.stargate.datagen.StargateAdvancements;
import dev.amblelabs.stargate.datagen.StargateLootTables;
import dev.amblelabs.stargate.datagen.recipe.StargateXplatRecipes;
import dev.amblelabs.stargate.fabric.datagen.tag.StargateBlockTagProvider;
import dev.amblelabs.stargate.fabric.datagen.tag.StargateItemTagProvider;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"SameParameterValue", "unused"})
public class FabricStargateDataGenerators implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var pack = gen.createPack();
        var tags = IXplatAbstractions.INSTANCE.tags();


        pack.addProvider((output, lookup) -> new StargateXplatRecipes(
                output, lookup, INGREDIENTS));

        var blockTags = new BlockTagProviderWrapper();
        pack.addProvider((output, lookup) -> blockTags.provider = new StargateBlockTagProvider(output, lookup, tags));
        pack.addProvider((output, lookup) -> new StargateItemTagProvider(output, lookup, blockTags.provider, tags));

        pack.addProvider((output, lookup) -> new LootTableProvider(
                output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(provider -> new StargateLootTables(),
                LootContextParamSets.ALL_PARAMS)), lookup
        ));

        pack.addProvider((output, lookup) -> new AdvancementProvider(
                output, lookup, List.of(new StargateAdvancements())
        ));

        pack.addProvider((output, lookup) -> new FabricStargateModelProvider(output));
    }

    private static class BlockTagProviderWrapper {
        StargateBlockTagProvider provider;
    }

    private static final IXplatIngredients INGREDIENTS = new IXplatIngredients() {

    };

    private static TagKey<Item> tag(String s) {
        return tag("c", s);
    }

    private static TagKey<Item> tag(String namespace, String s) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, s));
    }
}