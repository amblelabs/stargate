package dev.amble.stargate.client.api.state.stargate;

import dev.amble.stargate.api.state.stargate.MilkyWayState;
import dev.amble.stargate.client.models.MilkyWayGateModel;
import dev.amble.stargate.client.models.StargateModel;

public class ClientMilkyWayState extends ClientAbstractStargateState<MilkyWayGateModel> {

    public ClientMilkyWayState() {
        super(MilkyWayState.ID);
    }

    @Override
    protected MilkyWayGateModel createModel() {
        return StargateModel.create(MilkyWayGateModel::new);
    }
}
