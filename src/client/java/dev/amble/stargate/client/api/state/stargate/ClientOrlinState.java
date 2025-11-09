package dev.amble.stargate.client.api.state.stargate;

import dev.amble.stargate.api.state.stargate.OrlinState;
import dev.amble.stargate.client.models.OrlinGateModel;

public class ClientOrlinState extends ClientAbstractStargateState<OrlinGateModel> {

    public ClientOrlinState() {
        super(OrlinState.ID);

        this.portalSize = 14;
        this.portalYOffset = 1f;
    }

    @Override
    public OrlinGateModel createModel() {
        return new OrlinGateModel();
    }
}
