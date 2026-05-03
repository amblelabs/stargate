package dev.amblelabs.stargate.client.lib;

import dev.amblelabs.stargate.common.lib.amblekit.StargateEcs;

public class StargateClientEcs {

    public static void registerAll() {
        initState();
        initEvents();
        initBehavior();
    }

    public static void initState() {
        StargateEcs.initState();
    }

    public static void initEvents() {
        StargateEcs.initEvents();
    }

    public static void initBehavior() {
        StargateEcs.initBehavior();
    }
}
