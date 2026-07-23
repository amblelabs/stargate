package dev.amblelabs.stargate.interop;

import dev.amblelabs.stargate.xplat.IClientXplatAbstractions;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;

public class StargateInterop {

    public static void earlyInit() {
        IXplatAbstractions xplat = IXplatAbstractions.INSTANCE;
        xplat.initPlatformSpecific();
    }

    public static void init() {
    }

    public static void clientInit() {
        IClientXplatAbstractions.INSTANCE.initPlatformSpecific();
    }
}