package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.loot.AmbleBlockLootTable;
import dev.amble.lib.datagen.model.AmbleModelProvider;
import dev.amble.lib.datagen.sound.AmbleSoundProvider;
import dev.amble.lib.datagen.tag.AmbleBlockTagProvider;
import dev.amble.stargate.compat.modonomicon.ModonomiconCompat;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.world.StargateConfiguredFeature;
import dev.amble.stargate.world.StargatePlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SGDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        pack.addProvider((FabricDataGenerator.Pack.Factory<AmbleSoundProvider>) AmbleSoundProvider::new);

        pack.addProvider(AmbleBlockTagProvider::new).withBlocks(StargateBlocks.class);
        pack.addProvider(AmbleBlockLootTable::new).withBlocks(StargateBlocks.class);

        pack.addProvider(AmbleModelProvider::new)
                .withBlocks(StargateBlocks.class)
                .withItems(StargateItems.class, ModonomiconCompat.Items.class);

        pack.addProvider(SGRecipesGen::new);
        pack.addProvider(SGAdvancementGen::new);
        pack.addProvider(SGWorldGenGen::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, StargateConfiguredFeature::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, StargatePlacedFeatures::bootstrap);
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        pack.addProvider(SGEnglishLangGen::new);
    }
}