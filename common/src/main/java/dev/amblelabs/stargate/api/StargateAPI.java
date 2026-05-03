package dev.amblelabs.stargate.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface StargateAPI {
    String MOD_ID = "youre-fired";
    Logger LOGGER = LogManager.getLogger(MOD_ID);

    Supplier<StargateAPI> INSTANCE = Suppliers.memoize(() -> {
        try {
            return (StargateAPI) Class.forName("dev.amblelabs.yourefired.common.impl.YoureFiredAPIImpl")
                    .getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find AitAPIImpl, using a dummy");
            return new StargateAPI() { };
        }
    });

    static StargateAPI instance() {
        return INSTANCE.get();
    }

    static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
