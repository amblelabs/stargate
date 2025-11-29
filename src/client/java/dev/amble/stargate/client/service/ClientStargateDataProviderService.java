package dev.amble.stargate.client.service;

import dev.amble.stargate.api.data.StargateData;
import dev.amble.stargate.service.StargateDataProviderService;
import dev.amble.stargate.client.api.data.StargateClientData;

public class ClientStargateDataProviderService implements StargateDataProviderService {

    @Override
    public StargateData<?> getClient() {
        return StargateClientData.get();
    }
}
