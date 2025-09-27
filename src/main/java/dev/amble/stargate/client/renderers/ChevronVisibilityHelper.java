package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.kernels.impl.MilkyWayGateKernel;
import dev.amble.stargate.client.models.StargateModel;
import net.minecraft.client.model.ModelPart;

public class ChevronVisibilityHelper {
    private final StargateModel model;

    public ChevronVisibilityHelper(StargateModel model) {
        this.model = model;
    }

    public void setFromDialer(GateState state, StargateKernel.Impl kernel) {
        model.chev_light8.visible = false;
        model.chev_light9.visible = false;

        boolean visible = state instanceof GateState.Open || state instanceof GateState.PreOpen;
        int locked = (state instanceof GateState.Closed closed) ? closed.locked() : -1;

        ModelPart[] chevrons = new ModelPart[] {
                model.chev_light, model.chev_light2, model.chev_light3, model.chev_light4,
                model.chev_light5, model.chev_light6, model.chev_light7, model.chev_light7bottom
        };

        if (kernel instanceof MilkyWayGateKernel) {
            for (int i = 0; i < chevrons.length; i++) {
                chevrons[i].visible = visible || i < locked;
            }
        } else {
            for (ModelPart chevron : chevrons) {
                chevron.visible = visible;
            }
        }
    }
}