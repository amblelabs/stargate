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
@Config.Gui.Background("minecraft:textures/block/mud.png")
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

    public static FabricStargateConfig setup() {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();

        AutoConfig.register(FabricStargateConfig.class, PartitioningSerializer.wrap((cfg, clazz) ->
            new GsonConfigSerializer<>(cfg, clazz, gson)));

        FabricStargateConfig instance = AutoConfig.getConfigHolder(FabricStargateConfig.class).getConfig();

        StargateConfig.setCommon(instance.common);

        if (IXplatAbstractions.INSTANCE.isPhysicalClient()) {
            StargateConfig.setClient(instance.client);
        }

        StargateConfig.setServer(instance.server);
        return instance;
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
        private boolean useCustomMainMenu = DEFAULT_CUSTOM_MAIN_MENU;

        @Override
        public void validatePostLoad() {
        }

        @Override
        public boolean useCustomMainMenu() {
            return useCustomMainMenu;
        }
    }

    @Config(name = "server")
    public static final class Server implements StargateConfig.ServerConfigAccess, ConfigData {

        @Override
        public void validatePostLoad() {
        }
    }
}