package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.api.mod.StargateTags;
import dev.amblelabs.stargate.common.blocks.behavior.StargateComposting;
import dev.amblelabs.stargate.common.blocks.behavior.StargateStrippable;
import dev.amblelabs.stargate.common.lib.*;
import dev.amblelabs.stargate.interop.StargateInterop;
import dev.amblelabs.stargate.neoforge.network.NeoForgePacketHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(StargateAPI.MOD_ID)
public final class NeoForgeStargateInit {

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static IEventBus EVENT_BUS;

    public NeoForgeStargateInit(IEventBus modBus, ModContainer container) {
        EVENT_BUS = modBus;

        NeoForgeStargateConfig.setup(container);

        NeoForgePacketHandler.init(modBus);

        this.initListeners(modBus);

        StargateInterop.init();
        this.initRegistries(modBus);

        StargateComposting.setup();
        StargateStrippable.init();

        StargateInterop.init();
        StargateEcs.init();
    }

    private void initListeners(IEventBus eventBus) {
        eventBus.addListener((BuildCreativeModeTabContentsEvent ev) -> {
            StargateBlocks.registerCreativeTabBlocks(ev.getTabKey(), ev);
            StargateItems.registerCreativeTabItems(ev.getTabKey(), ev);
        });
    }

    private void initRegistries(IEventBus eventBus) {
        eventBus.addListener((DataPackRegistryEvent.NewRegistry ev) ->
                ev.dataPackRegistry(StargateRegistries.PROTOTYPE, Prototype.CODEC));

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
        StargateParticles.register();

        NeoForge.EVENT_BUS.addListener((WandererTradesEvent ev) -> ev.getRareTrades().add(new VillagerTrades.TreasureMapForEmeralds(
                14, StargateTags.Structures.ON_STARGATE_MAPS, "item.stargate.filled_map.stargate", // FIXME: use I18n class
                MapDecorationTypes.RED_X, 1, 10)
        ));

        StargatePlacementModifiers.register();
        StargateStructureTypes.register();
        StargateStructurePieces.register();

        StargateAttributes.register();
        StargateAdvancementTriggers.register();

        eventBus.addListener((EntityAttributeModificationEvent ev) -> ev.add(EntityType.PLAYER, StargateAttributes.SPACIAL_RESISTANCE));
    }
}
