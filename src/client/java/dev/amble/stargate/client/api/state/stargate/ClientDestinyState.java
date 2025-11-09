package dev.amble.stargate.client.api.state.stargate;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.state.stargate.DestinyState;
import dev.amble.stargate.client.models.DestinyGateModel;
import dev.amble.stargate.client.models.StargateModel;

public class ClientDestinyState extends ClientAbstractStargateState<DestinyGateModel> {

    public ClientDestinyState() {
        super(DestinyState.ID);

        this.portalType = StargateMod.id("textures/portal/white.png");
    }

    @Override
    protected DestinyGateModel createModel() {
        return StargateModel.create(DestinyGateModel::new);
    }
}
