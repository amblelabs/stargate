package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.RegisterClientStuff;
import dev.amblelabs.stargate.client.lib.StargateClientEcs;
import dev.amblelabs.stargate.client.renderers.RenderDeduper;
import dev.amblelabs.stargate.client.renderers.StargateAdditionalRenderers;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforgespi.locating.IModFile;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

@Mod(value = StargateAPI.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeStargateClientInit {

    public NeoForgeStargateClientInit(IEventBus eventBus, ModContainer container) {
        eventBus.addListener((AddPackFindersEvent ev) -> {
            IModFile modFile = ModList.get().getModFileById(StargateAPI.MOD_ID).getFile();
            Path packPath = modFile.findResource(
                    StargateAPI.PACK_MENU.withPrefix("resourcepacks/").getPath());

            PackSelectionConfig selectionConfig = new PackSelectionConfig(
                    true, // TODO: allow to disable the menu resourcepack
                    Pack.Position.TOP,
                    false
            );

            PackLocationInfo locationInfo = new PackLocationInfo(
                    StargateAPI.PACK_MENU.toString(),
                    Component.literal("Stargate Menu"), // FIXME: use I18n
                    PackSource.BUILT_IN,
                    Optional.empty()
            );

            PathPackResources.PathResourcesSupplier resourcesSupplier =
                    new PathPackResources.PathResourcesSupplier(packPath);

            Pack pack = Pack.readMetaAndCreate(
                    locationInfo,
                    resourcesSupplier,
                    PackType.CLIENT_RESOURCES,
                    selectionConfig
            );

            ev.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        });

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent ev) -> {
            if (ev.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) RenderDeduper.clear();
        });

        eventBus.addListener((RegisterGuiLayersEvent ev) -> ev.registerAboveAll(
                StargateAPI.modLoc("overlay"), StargateAdditionalRenderers::overlayGui));

        eventBus.addListener(this::clientInit);

        eventBus.addListener((RegisterParticleProvidersEvent ev) -> StargateParticles.FactoryHandler.registerFactories(
                new StargateParticles.FactoryHandler.Consumer() {

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                ev.registerSpriteSet(type, constructor::apply);
            }
        }));

        StargateInterop.clientInit();

        StargateClientEcs.registerAll();
    }

    private void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(RegisterClientStuff::init);
    }
}
