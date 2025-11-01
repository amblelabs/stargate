package dev.amble.stargate.client.api.behavior;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.tick.StargateTickEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.GateIdentityState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.event.render.StargateAnimateEvent;
import dev.amble.stargate.client.api.event.render.StargateRenderEvents;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.EmissionUtil;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.profiler.Profiler;

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
    public void render(Stargate stargate, ClientAbstractStargateState clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        if (!shouldRender(stargate, clientState)) return;

        this.customRender(stargate, clientState, entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    protected void customRender(Stargate stargate, ClientAbstractStargateState clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.4f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        profiler.push("glyphs");
        renderer.model.SymbolRing.roll = renderer.renderGlyphs(clientState, matrices, vertexConsumers, stargate, light, clientState.age);
        profiler.pop();

        profiler.push("animate");
        TEvents.handle(new StargateAnimateEvent(entity, stargate, renderer.model, clientState.age));
        profiler.pop();

        boolean anyVisible = this.updateChevronVisibility(stargate, renderer);

        profiler.push("render2layer");
        EmissionUtil.render2Layers(renderer.model, clientState.texture, clientState.emission, anyVisible, matrices, vertexConsumers, light, overlay);
        profiler.pop();
    }

    public boolean shouldRender(Stargate stargate, ClientAbstractStargateState state) {
        return clazz.isInstance(state);
    }

    public boolean updateChevronVisibility(Stargate stargate, StargateBlockEntityRenderer renderer) {
        renderer.model.chev_light8.visible = false;
        renderer.model.chev_light9.visible = false;

        GateState<?> state = stargate.getGateState();

        boolean visible = state.gateState() != GateState.StateType.CLOSED;
        int locked = state instanceof GateState.Closed closed ? closed.locked : -1;

        ModelPart[] chevrons = renderer.chevrons;

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i < locked;
        }

        return visible || locked != 0;
    }
}
