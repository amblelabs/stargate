package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateTags;
import dev.amblelabs.stargate.common.blocks.behavior.StargateComposting;
import dev.amblelabs.stargate.common.blocks.behavior.StargateStrippable;
import dev.amblelabs.stargate.common.lib.*;
import dev.amblelabs.stargate.fabric.network.FabricPacketHandler;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

import java.util.function.BiConsumer;

public final class FabricStargateInit implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricStargateConfig.setup();
        FabricPacketHandler.init();

        this.initListeners();

        StargateInterop.earlyInit();
        this.initRegistries();

        StargateComposting.setup();
        StargateStrippable.init();

        StargateInterop.init();
        StargateEcs.init();
    }

    private void initListeners() {
        CommandRegistrationCallback.EVENT.register((dispatcher, a, b) -> {
//            var root = AitCommands.root();
//            dispatcher.register(root);
        });

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            StargateBlocks.registerBlockCreativeTab(entries::accept, tab);
            StargateItems.registerItemCreativeTab(entries, tab);
        });
    }

    private void initRegistries() {
        StargateBlockSetTypes.registerBlocks(BlockSetType::register);

        StargateCreativeTabs.registerCreativeTabs(bind(BuiltInRegistries.CREATIVE_MODE_TAB));
        StargateFeatures.registerFeatures(bind(BuiltInRegistries.FEATURE));
        StargateSounds.registerSounds(bind(BuiltInRegistries.SOUND_EVENT));
        StargateBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK));
        StargateBlocks.registerBlockItems(bind(BuiltInRegistries.ITEM));
        StargateBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        StargateComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));
        StargateItems.registerItems(bind(BuiltInRegistries.ITEM));

        StargateRecipes.registerSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));
        StargateRecipes.registerTypes(bind(BuiltInRegistries.RECIPE_TYPE));

        StargateEntities.registerEntities(bind(BuiltInRegistries.ENTITY_TYPE));
//        AitAttributes.register(bind(BuiltInRegistries.ATTRIBUTE));
//        AitMobEffects.register(bind(BuiltInRegistries.MOB_EFFECT));
//        AitPotions.register(bind(BuiltInRegistries.POTION));

//        AitComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));

        StargateParticles.registerParticles(bind(BuiltInRegistries.PARTICLE_TYPE));

//        AitLootFunctions.registerSerializers(bind(BuiltInRegistries.LOOT_FUNCTION_TYPE));

        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, StargateAPI.modLoc("buried_stargate")));

        TradeOfferHelper.registerWanderingTraderOffers(1, listings -> {
            listings.add(new VillagerTrades.TreasureMapForEmeralds(
                    14, StargateTags.Structures.ON_STARGATE_MAPS, "item.stargate.filled_map.stargate",
                    MapDecorationTypes.RED_X, 1, 10)
            );
        });

        StargateFeatures.registerFeatures(bind(BuiltInRegistries.FEATURE));
        StargatePlacementModifiers.registerPlacementModifiers(bind(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE));
        StargateStructureTypes.registerStructureTypes(bind(BuiltInRegistries.STRUCTURE_TYPE));
        StargateStructurePieces.registerStructurePieces(bind(BuiltInRegistries.STRUCTURE_PIECE));

        this.dieInAFire();

//        AitStatistics.register();
    }

    private void dieInAFire() {
        FlammableBlockRegistry.getDefaultInstance();
    }

    private <T> BiConsumer<T, ResourceLocation> bind(Registry<T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}
