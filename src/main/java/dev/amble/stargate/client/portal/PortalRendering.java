package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientGenericGateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.LinkedList;
import java.util.Queue;

public class PortalRendering {
    public static Queue<StargateBlockEntity> PORTAL_RENDER_QUEUE = new LinkedList<>();

    public static void renderPortal(StargateBlockEntity stargateBlockEntity, MatrixStack stack) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        if (!stargateBlockEntity.hasStargate()) return;

        Stargate stargate = stargateBlockEntity.gate().get();
        ClientGenericGateState clientState = stargate.stateOrNull(ClientGenericGateState.state);

        if (clientState == null) return;

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));

        stack.translate(0, clientState.portalYOffset, 0);
        stack.scale(clientState.portalSize, clientState.portalSize, clientState.portalSize);

        BasicGateStates<?> state = stargate.getCurrentState();

        if (state == null)
            System.out.println(stargate.currentStateType());

        if (state.gateState() != BasicGateStates.StateType.CLOSED) {
            float time = ((MinecraftClient.getInstance().player.age / 200f) * 100f); // Slow down the animation

            RenderSystem.enableDepthTest();
            new PortalUtil().renderPortalInterior(stack, stargate, state, time);

            RenderSystem.disableDepthTest();
        }

        stack.pop();
        stack.pop();
    }
}
