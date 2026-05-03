package dev.amblelabs.stargate.interop;

import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.amblelabs.stargate.xplat.Platform;

import java.util.List;

public class StargateInterop {
//    public static final String PATCHOULI_ANY_INTEROP_FLAG = "ait:any_interop";

    public static final class Forge {
        public static final String CURIOS_API_ID = "curios";
    }

    public static final class Fabric {
        public static final String TRINKETS_API_ID = "trinkets";
    }

    public static void init() {
        initPatchouli();

        IXplatAbstractions xplat = IXplatAbstractions.INSTANCE;

        xplat.initPlatformSpecific();
    }

    public static void clientInit() {
        IClientXplatAbstractions.INSTANCE.initPlatformSpecific();
    }

    @SuppressWarnings({"RedundantOperationOnEmptyContainer", "ReassignedVariable", "ConstantValue"})
    private static void initPatchouli() {
        List<String> integrations = List.of();

        var anyInterop = false;
        for (var id : integrations) {
            if (IXplatAbstractions.INSTANCE.isModPresent(id)) {
                anyInterop = true;
                break;
            }
        }

        if (!anyInterop) {
            List<String> platformSpecificIntegrations;

            Platform platform = IXplatAbstractions.INSTANCE.platform();
            if (platform == Platform.FORGE) {
                platformSpecificIntegrations = List.of();
            } else if (platform == Platform.FABRIC) {
                platformSpecificIntegrations = List.of();
            } else {
                throw new UnsupportedOperationException();
            }

            for (var id : platformSpecificIntegrations) {
                if (IXplatAbstractions.INSTANCE.isModPresent(id)) {
                    anyInterop = true;
                    break;
                }
            }
        }

        if (anyInterop) {
//            PatchouliAPI.get().setConfigFlag(PATCHOULI_ANY_INTEROP_FLAG, true);
        }
    }
}