package dev.amble.stargate.api.v3.behavior.client;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientOrlinState;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.renderers.StargateRenderLayers;
import dev.amble.stargate.compat.DependencyChecker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import static dev.amble.stargate.client.renderers.StargateBlockEntityRenderer.ORLIN_GATE;

public class ClientOrlinBehavior extends ClientGenericGateBehavior {

    @Override
    protected void customRender(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.5f, 0.5f);

        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        boolean bl = stargate.getCurrentState().gateState() != BasicGateStates.StateType.CLOSED;

        Identifier texture = StargateBlockEntityRenderer.getTextureForGate(stargate);
        Identifier emission = StargateBlockEntityRenderer.getEmissionForGate(stargate);

        if (bl && DependencyChecker.hasIris())
            ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);

        ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, overlay, 1, 1, 1, 1);

        if (bl && !DependencyChecker.hasIris())
            ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);
    }

    @Override
    public boolean shouldOverride(Stargate stargate) {
        return stargate.stateOrNull(ClientOrlinState.state) instanceof ClientOrlinState;
    }
}
