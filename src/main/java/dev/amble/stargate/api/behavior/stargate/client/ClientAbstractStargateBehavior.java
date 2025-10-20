package dev.amble.stargate.api.behavior.stargate.client;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.render.StargateRenderEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.renderers.StargateRenderLayers;
import dev.amble.stargate.compat.DependencyChecker;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.state.TState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Supplier;

public abstract class ClientAbstractStargateBehavior<T extends ClientAbstractStargateState> implements TBehavior, StargateRenderEvents, StargateLoadedEvents {

    private final Class<T> clazz;
    private final TState.Type<? super T> type;
    private final Supplier<T> stateSupplier;

    public ClientAbstractStargateBehavior(Class<T> clazz, TState.Type<? super T> type, Supplier<T> supplier) {
        this.clazz = clazz;
        this.type = type;
        this.stateSupplier = supplier;
    }

    @Override
    public void onLoaded(Stargate stargate) {
        if (stargate.isClient())
            stargate.addState(this.stateSupplier.get());
    }

    @Override
    public void render(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        if (!shouldRender(stargate)) return;

        this.customRender(stargate, entity, renderer, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    protected void customRender(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.4f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

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

        boolean bl = stargate.getGateState().gateState() != GateState.StateType.CLOSED;

        renderer.model.chev_light7.visible = bl;
        renderer.model.chev_light7bottom.visible = bl;

        if (DependencyChecker.hasIris())
            renderer.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);

        renderer.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, overlay, 1, 1, 1, 1);

        if (!DependencyChecker.hasIris())
            renderer.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, 1, 1, 1);
    }

    public boolean shouldRender(Stargate stargate) {
        return clazz.isInstance(stargate.state(type));
    }

    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        boolean visible = stargate.getGateState().gateState() != GateState.StateType.CLOSED;

        for (ModelPart chevron : renderer.chevrons) {
            chevron.visible = visible;
        }
    }
}
