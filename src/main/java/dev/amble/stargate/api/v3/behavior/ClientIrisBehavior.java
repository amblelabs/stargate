package dev.amble.stargate.api.v3.behavior;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.event.render.StargateRenderEvents;
import dev.amble.stargate.api.v3.state.ClientIrisState;
import dev.amble.stargate.api.v3.state.IrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AnimationState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientIrisBehavior implements TBehavior, StargateBlockEvents, StargateRenderEvents {

    @Override
    public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
        if (!world.isClient())
            return;

        IrisState irisState = stargate.state(IrisState.state);
        ClientIrisState clientIrisState = stargate.state(ClientIrisState.state);

        AnimationState openState = clientIrisState.OPEN_STATE;
        AnimationState closeState = clientIrisState.CLOSE_STATE;
        int age = entity.age;

//        if (gate.state() instanceof GateState.Closed closed && closed.locking()) {
//            clientIrisState.CLOSE_STATE.stop();
//            clientIrisState.OPEN_STATE.stop();
//            return;
//        }

        if (irisState.open) {
            closeState.startIfNotRunning(age);
            openState.stop();
            clientIrisState.stopOpening = false;
        } else {
            if (!clientIrisState.stopOpening) {
                closeState.stop();
                openState.startIfNotRunning(age);
            }

            if (openState.isRunning()) {
                ClientScheduler.get().runTaskLater(() -> {
                    openState.stop();
                    clientIrisState.stopOpening = true;
                }, TimeUnit.SECONDS, 3);
            }
        }
    }

    @Override
    public void render(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        ClientIrisState clientIrisState = stargate.state(ClientIrisState.state);
        boolean renderIris = clientIrisState.CLOSE_STATE.isRunning() || clientIrisState.OPEN_STATE.isRunning();

        // TODO: make the iris a separate thingy
        if (renderer.model.getChild("iris").isPresent())
            renderer.model.iris.visible = renderIris;
    }
}
