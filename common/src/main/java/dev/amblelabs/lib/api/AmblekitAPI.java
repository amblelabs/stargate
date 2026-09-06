package dev.amblelabs.lib.api;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface AmblekitAPI {
    String MOD_ID = "amblekit";
    Logger LOGGER = LogManager.getLogger(MOD_ID);

    Supplier<AmblekitAPI> INSTANCE = Suppliers.memoize(() -> {
        try {
            return (AmblekitAPI) Class.forName("dev.amblelabs.lib.common.impl.AmblekitAPIImpl")
                    .getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find AmblekitAPIImpl, using a dummy");
            return new AmblekitAPI() { };
        }
    });

    static AmblekitAPI instance() {
        return INSTANCE.get();
    }

    static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
