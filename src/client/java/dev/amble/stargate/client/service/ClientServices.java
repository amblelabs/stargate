package dev.amble.stargate.client.service;

import dev.amble.stargate.service.Services;

public class ClientServices {

    public static void init() {
        Services.GATES = new ClientStargateDataProviderService();
        Services.TOOLTIP = new TooltipServiceImpl();
        Services.WORLDS = new ClientWorldProviderService();
    }
}
