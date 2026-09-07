package dev.amblelabs.stargate.fabric;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.client.RegisterClientStuff;
import dev.amblelabs.stargate.client.lib.StargateClientEcs;
import dev.amblelabs.stargate.client.renderers.*;
import dev.amblelabs.stargate.common.lib.StargateParticles;
import dev.amblelabs.stargate.fabric.network.FabricPacketHandler;
import dev.amblelabs.stargate.interop.StargateInterop;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class FabricStargateClientInit implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricPacketHandler.initClient();

        FabricLoader.getInstance().getModContainer(StargateAPI.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(StargateAPI.modLoc("menu"), container, ResourcePackActivationType.DEFAULT_ENABLED));

        WorldRenderEvents.START.register(context -> RenderDeduper.clear());
        HudRenderCallback.EVENT.register(StargateAdditionalRenderers::overlayGui);

        RegisterClientStuff.init();

        StargateParticles.FactoryHandler.registerFactories(new StargateParticles.FactoryHandler.Consumer() {

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                ParticleFactoryRegistry.getInstance().register(type, constructor::apply);
            }
        });

        StargateInterop.clientInit();

        StargateClientEcs.registerAll();
    }
}
