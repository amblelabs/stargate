package dev.amble.stargate.client.api.behavior;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.block.StargateBlockTickEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.iris.IrisState;
import dev.amble.stargate.api.state.iris.ClientIrisState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.animations.StargateAnimations;
import dev.amble.stargate.client.api.event.render.StargateAnimateEvents;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.amble.stargate.client.models.StargateModel;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientIrisBehavior implements TBehavior, StargateBlockTickEvents, StargateAnimateEvents {

    @Override
    public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
        if (!world.isClient())
            return;

        ClientAbstractStargateState clientState = stargate.resolveState(ClientAbstractStargateState.state);

        IrisState irisState = stargate.state(IrisState.state);
        ClientIrisState clientIrisState = stargate.state(ClientIrisState.state);

        if (clientIrisState.ticks > 0) clientIrisState.ticks--;
        else clientIrisState.stopOpening = true;

        GateState.Closed closed = stargate.stateOrNull(GateState.Closed.state);

        if (closed != null && closed.locking) {
            clientIrisState.CLOSE_STATE.stop();
            clientIrisState.OPEN_STATE.stop();
            return;
        }

        AnimationState openState = clientIrisState.OPEN_STATE;
        AnimationState closeState = clientIrisState.CLOSE_STATE;
        int age = clientState.age;

        if (irisState.open) {
            closeState.startIfNotRunning(age);
            openState.stop();
            clientIrisState.stopOpening = false;
        } else {
            if (!clientIrisState.stopOpening) {
                closeState.stop();
                openState.startIfNotRunning(age);
            }

            if (openState.isRunning()) clientIrisState.ticks = 3 * 20;
        }
    }

    @Override
    public void animate(StargateBlockEntity stargateBlockEntity, Stargate stargate, BaseStargateModel baseModel, int age) {
        if (!(baseModel instanceof StargateModel model)) return;

        ClientIrisState clientIrisState = stargate.stateOrNull(ClientIrisState.state);
        boolean renderIris = false;

        if (clientIrisState != null) {
            renderIris = clientIrisState.CLOSE_STATE.isRunning() || clientIrisState.OPEN_STATE.isRunning();

            model.updateAnimation(clientIrisState.CLOSE_STATE, StargateAnimations.IRIS_CLOSE, age);
            model.updateAnimation(clientIrisState.OPEN_STATE, StargateAnimations.IRIS_OPEN, age);
        }

        // TODO: make the iris a separate model
        model.iris.visible = renderIris;
    }
}
