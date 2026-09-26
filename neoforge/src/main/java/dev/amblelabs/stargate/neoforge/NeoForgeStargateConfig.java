package dev.amblelabs.stargate.neoforge;

import dev.amblelabs.stargate.api.mod.StargateConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeStargateConfig implements StargateConfig.CommonConfigAccess {

    public static void setup(ModContainer container) {
        var config = new ModConfigSpec.Builder().configure(NeoForgeStargateConfig::new);
        var clientConfig = new ModConfigSpec.Builder().configure(NeoForgeStargateConfig.Client::new);
        var serverConfig = new ModConfigSpec.Builder().configure(NeoForgeStargateConfig.Server::new);
        StargateConfig.setCommon(config.getLeft());
        StargateConfig.setClient(clientConfig.getLeft());
        StargateConfig.setServer(serverConfig.getLeft());

        container.registerConfig(ModConfig.Type.COMMON, config.getRight());
        container.registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
        container.registerConfig(ModConfig.Type.SERVER, serverConfig.getRight());
    }

    public NeoForgeStargateConfig(ModConfigSpec.Builder builder) {

    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static class Client implements StargateConfig.ClientConfigAccess {

        private static ModConfigSpec.BooleanValue renderOverlay;
        private static ModConfigSpec.BooleanValue renderPuddleBackground;

        private static ModConfigSpec.IntValue puddleCycleTicks;
        private static ModConfigSpec.IntValue puddleParticleTick;

        public Client(ModConfigSpec.Builder builder) {
            renderOverlay = builder.comment("TODO")
                    .define("renderOverlay", DEFAULT_RENDER_OVERLAY);

            renderPuddleBackground = builder.comment("TODO")
                    .define("renderPuddleBackground", DEFAULT_RENDER_PUDDLE_BACKGROUND);

            puddleCycleTicks = builder.comment("TODO")
                    .defineInRange("puddleCycleTick", DEFAULT_PUDDLE_CYCLE_TICKS, MIN_PUDDLE_CYCLE_TICKS, MAX_PUDDLE_PARTICLE_TICK);

            puddleParticleTick = builder.comment("TODO")
                    .defineInRange("puddleParticleTick", DEFAULT_PUDDLE_PARTICLE_TICK, MIN_PUDDLE_PARTICLE_TICK, MAX_PUDDLE_PARTICLE_TICK);
        }

        @Override
        public boolean renderOverlay() {
            return renderOverlay.getAsBoolean();
        }

        @Override
        public boolean renderPuddleBackground() {
            return renderPuddleBackground.getAsBoolean();
        }

        @Override
        public int puddleCycleTicks() {
            return puddleCycleTicks.getAsInt();
        }

        @Override
        public int puddleParticleTick() {
            return puddleParticleTick.getAsInt();
        }
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static class Server implements StargateConfig.ServerConfigAccess {

        private static ModConfigSpec.IntValue worldGenStargateDistance;

        public Server(ModConfigSpec.Builder builder) {
            worldGenStargateDistance = builder.comment("TODO")
                    .defineInRange("worldGenStargateDistance", DEFAULT_WORLD_GEN_STARGATE_DISTANCE, MIN_WORLD_GEN_STARGATE_DISTANCE, MAX_WORLD_GEN_STARGATE_DISTANCE);
        }

        @Override
        public int worldGenStargateDistance() {
            return worldGenStargateDistance.getAsInt();
        }
    }
}
