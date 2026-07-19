package dev.amblelabs.stargate.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.resources.ResourceLocation;

@Config(name = StargateAPI.MOD_ID)
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class FabricStargateConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("common")
    @ConfigEntry.Gui.TransitiveObject
    public final Common common = new Common();

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public final Client client = new Client();

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public final Server server = new Server();

    public static void setup() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();

        AutoConfig.register(FabricStargateConfig.class, PartitioningSerializer.wrap((cfg, clazz) ->
            new GsonConfigSerializer<>(cfg, clazz, gson)));

        FabricStargateConfig instance = AutoConfig.getConfigHolder(FabricStargateConfig.class).getConfig();

        StargateConfig.setCommon(instance.common);

        if (IXplatAbstractions.INSTANCE.isPhysicalClient())
            StargateConfig.setClient(instance.client);

        StargateConfig.setServer(instance.server);
    }

    @Config(name = "common")
    public static final class Common implements StargateConfig.CommonConfigAccess, ConfigData {

        @Override
        public void validatePostLoad() {
        }
    }

    @Config(name = "client")
    public static final class Client implements StargateConfig.ClientConfigAccess, ConfigData {

        @ConfigEntry.Gui.Tooltip
        private boolean renderPuddleBackground = DEFAULT_RENDER_PUDDLE_BACKGROUND;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = MIN_PUDDLE_CYCLE_TICKS, max = MAX_PUDDLE_CYCLE_TICKS)
        private int puddleCycleTicks = DEFAULT_PUDDLE_CYCLE_TICKS;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = MIN_PUDDLE_PARTICLE_TICK, max = MAX_PUDDLE_PARTICLE_TICK)
        private int puddleParticleTick = DEFAULT_PUDDLE_PARTICLE_TICK;

        @Override
        public void validatePostLoad() {
            this.puddleParticleTick = Math.clamp(puddleParticleTick, MIN_PUDDLE_PARTICLE_TICK, MAX_PUDDLE_PARTICLE_TICK);
        }

        @Override
        public boolean renderPuddleBackground() {
            return renderPuddleBackground;
        }

        @Override
        public int puddleCycleTicks() {
            return puddleCycleTicks;
        }

        @Override
        public int puddleParticleTick() {
            return puddleParticleTick;
        }
    }

    @Config(name = "server")
    public static final class Server implements StargateConfig.ServerConfigAccess, ConfigData {

        @Override
        public void validatePostLoad() {
        }
    }
}