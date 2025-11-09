package dev.amble.stargate.client.api.state.stargate;

import dev.amble.stargate.api.state.stargate.PegasusState;
import dev.amble.stargate.client.models.PegasusGateModel;
import dev.amble.stargate.client.models.StargateModel;

public class ClientPegasusState extends ClientAbstractStargateState<PegasusGateModel> {

    public ClientPegasusState() {
        super(PegasusState.ID);

        this.glyphColor = 0xffffff;
    }

    @Override
    protected PegasusGateModel createModel() {
        return StargateModel.create(PegasusGateModel::new);
    }
}
