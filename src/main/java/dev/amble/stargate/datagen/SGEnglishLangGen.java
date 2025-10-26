package dev.amble.stargate.datagen;

import dev.amble.lib.datagen.lang.AmbleLanguageProvider;
import dev.amble.lib.datagen.lang.LanguageType;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class SGEnglishLangGen extends AmbleLanguageProvider {

    public SGEnglishLangGen(FabricDataOutput output) {
        super(output, LanguageType.EN_US);

        // Blocks
        translateBlocks(StargateBlocks.class);
        addTranslation(StargateBlocks.DHD, "Dial-Home Device");
        addTranslation(StargateBlocks.GENERIC_GATE, "Stargate");
        addTranslation(StargateBlocks.ORLIN_GATE, "Orlin Stargate");
        addTranslation(StargateBlocks.NAQUADAH_BLOCK, "Block of Naquadah");
        addTranslation(StargateBlocks.RAW_NAQUADAH_BLOCK, "Block of Raw Naquadah");
        addTranslation(StargateBlocks.NAQUADAH_ORE, "Naquadah Ore");
        addTranslation(StargateBlocks.TOASTER, "Toaster");

        // Items
        translateItems(StargateItems.class);
        addTranslation(StargateItems.ADDRESS_CARTOUCHE, "Address Cartouche");
        addTranslation(StargateItems.IRIS_BLADE, "Iris Blade");
        addTranslation(StargateItems.IRIS_FRAME, "Iris Frame");
        addTranslation(StargateItems.IRIS, "Iris");
        addTranslation(StargateItems.RAW_NAQUADAH, "Raw Naquadah");
        addTranslation(StargateItems.NAQUADAH_INGOT, "Naquadah Ingot");
        addTranslation(StargateItems.NAQUADAH_NUGGET, "Naquadah Nugget");
        addTranslation(StargateItems.LIQUID_NAQUADAH, "Liquid Naquadah Container");
        addTranslation(StargateItems.EMPTY_CONTAINER, "Empty Container");
        addTranslation(StargateItems.COPPER_COIL, "Copper Wire");
        addTranslation(StargateItems.CONTROL_CRYSTAL_BLUE, "Blue Control Crystal");
        addTranslation(StargateItems.CONTROL_CRYSTAL_YELLOW, "Yellow Control Crystal");
        addTranslation(StargateItems.CONTROL_CRYSTAL_RED, "Red Control Crystal");
        addTranslation(StargateItems.CONTROL_CRYSTAL_MASTER, "Master Control Crystal");
        addTranslation(StargateItems.TOAST, "Toast");
        addTranslation(StargateItems.BURNT_TOAST, "Burnt Toast");

        // Stargate Items
        addTranslation(StargateItems.MILKY_WAY_STARGATE, "Milky Way Stargate");
        addTranslation(StargateItems.ORLIN_STARGATE, "Orlin Stargate");
        addTranslation(StargateItems.PEGASUS_STARGATE, "Pegasus Stargate");
        addTranslation(StargateItems.DESTINY_STARGATE, "Destiny Stargate");

        //Misc
        addTranslation("itemGroup.stargate.item_group", "STARGATE");

        addTranslation("tooltip.stargate.link_item.holdformoreinfo", "Hold shift for more info");
        addTranslation("tooltip.stargate.dialer.hint", "Use on stargate to link, then use on another stargate to dial");

        addTranslation("text.stargate.gate", "STARGATE");
        addTranslation("text.stargate.linked", "Linked to: ");

        addTranslation("death.attack.stargate_flow", "%s went against the flow");
        addTranslation("attribute.stargate.spacial_resistance", "Spacial Resistance");
        addTranslation("effect.stargate.spacial_dynamic", "Spacial Dynamic");

        // Commands
        addTranslation("command.stargate.generic.missing", "No Stargate found!");
        addTranslation("command.stargate.generic.unavailable", "This Stargate is not available!");

        //Achievements
        addTranslation("achievement.stargate.title.root", "Stargate Mod");
        addTranslation("achievement.stargate.description.root", "Begin your journey through the stars!");

        addTranslation("achievement.stargate.title.obtain_raw_naquadah", "The Fifth Race");
        addTranslation("achievement.stargate.description.obtain_raw_naquadah", "Discover the element the Stargates are made of.");

        addTranslation("achievement.stargate.title.obtain_address_cartouche", "It was right in front of us!");
        addTranslation("achievement.stargate.description.obtain_address_cartouche", "Create an Address Cartouche to dial the Stargate Address.");

        addTranslation("achievement.stargate.title.obtain_liquid_naquadah", "Enough power to dial the Eighth Chevron...?!");
        addTranslation("achievement.stargate.description.obtain_liquid_naquadah", "Create or find, then put Liquid Naquadah into a Container.");

        addTranslation("achievement.stargate.title.obtain_toaster", "You'll need a new one after this!");
        addTranslation("achievement.stargate.description.obtain_toaster", "Make a toaster for the Orlin gate.");

        addTranslation("achievement.stargate.title.obtain_burnt_toast", "Bit too long in the toaster");
        addTranslation("achievement.stargate.description.obtain_burnt_toast", "Obtain burnt toast!");

        addTranslation("achievement.stargate.title.passed_through", "It'll take you a million light years away from home");
        addTranslation("achievement.stargate.description.passed_through", "Travel through a Stargate for the first time.");
    }
}
