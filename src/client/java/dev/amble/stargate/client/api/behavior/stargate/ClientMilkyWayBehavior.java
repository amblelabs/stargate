package dev.amble.stargate.client.api.behavior.stargate;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.state.stargate.MilkyWayState;
import dev.amble.stargate.api.state.stargate.PegasusState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.state.stargate.ClientMilkyWayState;
import dev.amble.stargate.client.api.state.stargate.ClientPegasusState;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

public class ClientMilkyWayBehavior extends ClientAbstractStargateBehavior.Spinning<ClientMilkyWayState> implements StargateLoadedEvents {

    public ClientMilkyWayBehavior() {
        super(MilkyWayState.class, ClientMilkyWayState.class);
    }

    @Override
    protected ClientMilkyWayState createClientState(Stargate stargate) {
        return new ClientMilkyWayState();
    }

 @Override
 protected void preRender(Stargate stargate, ClientMilkyWayState clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
     super.preRender(stargate, clientState, entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);

     /// Temp disabled cus the gate should only spin if a "Dialing computer" is present - Addie
     /// maybe put some check here for if there is a dialing computer linked?
     /// I also see that this handles the glyph rendering so it has no glyphs rn, womp ig 🤷‍♂️

   // ModelPart symbolRing = clientState.model().SymbolRing;
   // symbolRing.roll = renderGlyphs(symbolRing.roll, 180f, clientState, matrices, vertexConsumers, stargate, light);
 }
}

