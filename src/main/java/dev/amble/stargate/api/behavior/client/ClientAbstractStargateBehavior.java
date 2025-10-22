package dev.amble.stargate.api.behavior.client;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.render.StargateAnimateEvent;
import dev.amble.stargate.api.event.render.StargateRenderEvents;
import dev.amble.stargate.api.event.tick.StargateTickEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.GateIdentityState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.EmissionUtil;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public abstract class ClientAbstractStargateBehavior<T extends ClientAbstractStargateState> implements TBehavior, StargateRenderEvents, StargateLoadedEvents, StargateTickEvents {

    private final Class<T> clazz;
    private final Class<? extends GateIdentityState> identity;

    public ClientAbstractStargateBehavior(Class<? extends GateIdentityState> identity, Class<T> clazz) {
        this.clazz = clazz;
        this.identity = identity;
    }

    protected abstract T createClientState(Stargate stargate);

    @Override
    public void onLoaded(Stargate stargate) {
        if (stargate.isClient() && identity.isInstance(stargate.state(GateIdentityState.state)))
            stargate.addState(this.createClientState(stargate));
    }

    @Override
    public void tick(Stargate stargate) {
        if (!stargate.isClient()) return;

        stargate.resolveState(ClientAbstractStargateState.state).age++;
    }

    @Override
    public void render(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        ClientAbstractStargateState clientState = stargate.resolveState(ClientAbstractStargateState.state);

        if (!shouldRender(stargate, clientState)) return;

        this.customRender(stargate, entity, renderer, clientState, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    protected void customRender(Stargate stargate, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, ClientAbstractStargateState clientState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.4f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        renderer.model.SymbolRing.roll = renderer.renderGlyphs(matrices, vertexConsumers, stargate, light, clientState.age);
        renderer.animate(entity, stargate, clientState.age);

        renderer.model.chev_light8.visible = false;
        renderer.model.chev_light9.visible = false;

        this.updateChevronVisibility(stargate, renderer);

        boolean bl = stargate.getGateState().gateState() != GateState.StateType.CLOSED;

        renderer.model.chev_light7.visible = bl;
        renderer.model.chev_light7bottom.visible = bl;

        EmissionUtil.render2Layers(renderer.model, clientState.texture, clientState.emission, true, matrices, vertexConsumers, light, overlay);
    }

    public boolean shouldRender(Stargate stargate, ClientAbstractStargateState state) {
        return clazz.isInstance(stargate.state(ClientAbstractStargateState.state));
    }

    public void updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        boolean visible = stargate.getGateState().gateState() != GateState.StateType.CLOSED;

        for (ModelPart chevron : renderer.chevrons) {
            chevron.visible = visible;
        }
    }
}
