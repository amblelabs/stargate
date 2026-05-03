package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.common.blocks.behavior.StargateComposting;
import dev.amblelabs.stargate.common.blocks.behavior.StargateStrippable;
import dev.amblelabs.stargate.common.lib.*;
import dev.amblelabs.stargate.fabric.network.FabricPacketHandler;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.BiConsumer;

public final class FabricStargateInit implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricStargateConfig.setup();
        FabricPacketHandler.init();

        this.initListeners();
        this.initRegistries();

        StargateComposting.setup();
        StargateStrippable.init();

        StargateInterop.init();
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

        StargateSounds.registerSounds(bind(BuiltInRegistries.SOUND_EVENT));
        StargateBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK));
        StargateBlocks.registerBlockItems(bind(BuiltInRegistries.ITEM));
//        AitBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
        StargateItems.registerItems(bind(BuiltInRegistries.ITEM));

//        AitEntities.registerEntities(bind(BuiltInRegistries.ENTITY_TYPE));
//        AitAttributes.register(bind(BuiltInRegistries.ATTRIBUTE));
//        AitMobEffects.register(bind(BuiltInRegistries.MOB_EFFECT));
//        AitPotions.register(bind(BuiltInRegistries.POTION));

//        AitComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));

        StargateParticles.registerParticles(bind(BuiltInRegistries.PARTICLE_TYPE));

//        AitLootFunctions.registerSerializers(bind(BuiltInRegistries.LOOT_FUNCTION_TYPE));

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
