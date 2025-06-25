package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.lang.LanguageType;
import dev.amble.lib.datagen.lang.AmbleLanguageProvider;
import dev.amble.lib.datagen.loot.AmbleBlockLootTable;
import dev.amble.lib.datagen.model.AmbleModelProvider;
import dev.amble.lib.datagen.sound.AmbleSoundProvider;
import dev.amble.lib.datagen.tag.AmbleBlockTagProvider;
import dev.amble.stargate.core.StargateBlocks;
import dev.amble.stargate.core.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SGDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        genSounds(pack);
        genTags(pack);
        genLoot(pack);
        genModels(pack);
    }

    private void genModels(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleModelProvider provider = new AmbleModelProvider(output);

            provider.withBlocks(StargateBlocks.class);
            provider.withItems(StargateItems.class);

            return provider;
        })));
    }
    private void genTags(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> new AmbleBlockTagProvider(output, registriesFuture).withBlocks(StargateBlocks.class))));
    }
    private void genLoot(FabricDataGenerator.Pack pack) {
         pack.addProvider((((output, registriesFuture) -> new AmbleBlockLootTable(output).withBlocks(StargateBlocks.class))));
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleLanguageProvider provider = new AmbleLanguageProvider(output, LanguageType.EN_US);

            provider.translateBlocks(StargateBlocks.class);
            provider.addTranslation(StargateBlocks.DHD, "Dial-Home Device");
            provider.addTranslation(StargateBlocks.STARGATE, "Stargate");

            provider.translateItems(StargateItems.class);
            provider.addTranslation(StargateItems.SPECTRAL_PROJECTOR, "Address Cartouche");

            provider.addTranslation("itemGroup.stargate.item_group", "STARGATE");

            provider.addTranslation("tooltip.stargate.link_item.holdformoreinfo", "Hold shift for more info");
            provider.addTranslation("tooltip.stargate.dialer.hint", "Use on stargate to link, then use on another stargate to dial");

            return provider;
        })));
    }

    private void genSounds(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleSoundProvider provider = new AmbleSoundProvider(output);

            return provider;
        })));
    }
}