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
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

import java.util.HashSet;
import java.util.Set;

public final class FabricStargateInit implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricStargateConfig.setup();

        FabricPacketHandler.initPackets();
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

        Set<ResourceKey<CreativeModeTab>> tabKeys = new HashSet<>(StargateBlocks.getCreativeTabs());
        tabKeys.addAll(StargateItems.getCreativeTabs());

        for (ResourceKey<CreativeModeTab> tabKey : tabKeys) {
            ItemGroupEvents.modifyEntriesEvent(tabKey).register(entries -> {
                StargateBlocks.registerCreativeTabBlocks(tabKey, entries);
                StargateItems.registerCreativeTabItems(tabKey, entries);
            });
        }
    }

    private void initRegistries() {
        StargateBlockSetTypes.registerBlocks(BlockSetType::register);

        StargateCreativeTabs.register();
        StargateFeatures.register();
        StargateSounds.register();
        StargateBlocks.register();
        StargateBlockEntities.register();
        StargateComponents.register();
        StargateItems.register();

        StargateRecipes.register();

        StargateEntities.register();
//        AitAttributes.register(bind(BuiltInRegistries.ATTRIBUTE));
//        AitMobEffects.register(bind(BuiltInRegistries.MOB_EFFECT));
//        AitPotions.register(bind(BuiltInRegistries.POTION));

//        AitComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));

        StargateParticles.register();

//        AitLootFunctions.registerSerializers(bind(BuiltInRegistries.LOOT_FUNCTION_TYPE));

        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, StargateAPI.modLoc("buried_stargate")));

        TradeOfferHelper.registerWanderingTraderOffers(1, listings -> {
            listings.add(new VillagerTrades.TreasureMapForEmeralds(
                    14, StargateTags.Structures.ON_STARGATE_MAPS, "item.stargate.filled_map.stargate",
                    MapDecorationTypes.RED_X, 1, 10)
            );
        });

        StargateFeatures.register();
        StargatePlacementModifiers.register();
        StargateStructureTypes.register();
        StargateStructurePieces.register();

        StargateAttributes.register();

        FabricDefaultAttributeRegistry.register(EntityType.PLAYER,
                Player.createAttributes().add(StargateAttributes.SPACIAL_RESISTANCE));

        this.dieInAFire();

//        AitStatistics.register();
    }

    private void dieInAFire() {
        FlammableBlockRegistry.getDefaultInstance();
    }
}
