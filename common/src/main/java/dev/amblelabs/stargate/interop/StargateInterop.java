package dev.amblelabs.stargate.interop;

import dev.amblelabs.stargate.xplat.ClientXplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatAbstractions;

public class StargateInterop {

    public static void earlyInit() {
        XplatAbstractions xplat = XplatAbstractions.INSTANCE;
        xplat.initPlatformSpecific();
    }

    public static void init() {
    }

    public static void clientInit() {
        ClientXplatAbstractions.INSTANCE.initPlatformSpecific();
    }
}