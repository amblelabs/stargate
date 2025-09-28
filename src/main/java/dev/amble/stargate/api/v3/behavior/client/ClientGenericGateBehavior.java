package dev.amble.stargate.api.v3.behavior.client;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.render.StargateRenderEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientGenericGateState;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.renderers.StargateRenderLayers;
import dev.amble.stargate.compat.DependencyChecker;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class ClientGenericGateBehavior implements TBehavior, StargateRenderEvents {
    @Override
    public void render(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        if (!shouldOverride(stargate)) return;

        this.customRender(stargate, entity, renderer, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    protected void customRender(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.4f, 0.5f);

        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        Identifier texture = StargateBlockEntityRenderer.getTextureForGate(stargate);
        Identifier emission = StargateBlockEntityRenderer.getEmissionForGate(stargate);

        renderer.model.SymbolRing.roll = renderer.renderGlyphs(matrices, vertexConsumers, stargate, light, entity.age);
        renderer.model.animateStargateModel(entity, stargate, entity.age);

        renderer.model.chev_light8.visible = false;
        renderer.model.chev_light9.visible = false;

        this.updateChevronVisibility(stargate, renderer);

        boolean bl = stargate.getCurrentState().gateState() != BasicGateStates.StateType.CLOSED;

        renderer.model.chev_light7.visible = bl;
        renderer.model.chev_light7bottom.visible = bl;

        if (DependencyChecker.hasIris()) {
            renderer.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);
        }

        renderer.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, overlay, 1, 1, 1, 1);

        if (!DependencyChecker.hasIris()) {
            renderer.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);
        }
    }

    public boolean shouldOverride(Stargate stargate) {
        ClientGenericGateState state = stargate.stateOrNull(ClientGenericGateState.state);
        return state != null && !state.custom;
    }

    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        boolean visible = stargate.getCurrentState().gateState() != BasicGateStates.StateType.CLOSED;

        for (ModelPart chevron : renderer.chevrons) {
            chevron.visible = visible;
        }
    }
}
