package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.api.v3.state.client.ClientGenericGateState;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.Queue;

public class PortalRendering {
    public static Queue<StargateBlockEntity> QUEUE = new LinkedList<>();

    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        MatrixStack stack = context.matrixStack();
        float time = client.player.age / 200f * 100f; // Slow down the animation

        while (!PortalRendering.QUEUE.isEmpty()) {
            StargateBlockEntity gate = PortalRendering.QUEUE.poll();
            if (gate == null || !gate.hasStargate()) continue;

            Stargate stargate = gate.gate().get();
            ClientGenericGateState clientState = stargate.stateOrNull(ClientGenericGateState.state);

            if (clientState == null) continue;

            render(client, stack, gate, stargate, clientState, time);
        }
    }

    public static void render(MinecraftClient client, MatrixStack stack, StargateBlockEntity entity, Stargate stargate, ClientGenericGateState clientState, float age) {
        Vec3d pos = entity.getPos().toCenterPos()
                .subtract(client.gameRenderer.getCamera().getPos());

        stack.push();
        stack.translate(pos.getX(), pos.getY(), pos.getZ());
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getCachedState().get(StargateBlock.FACING).asRotation()));
        stack.translate(0, -2f, 0);

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));

        stack.translate(0, clientState.portalYOffset, 0);
        stack.scale(clientState.portalSize, clientState.portalSize, clientState.portalSize);

        BasicGateStates<?> state = stargate.getCurrentState();

        // can this be moved outside the loop?
        RenderSystem.enableDepthTest();

        new PortalUtil().renderPortalInterior(stack, stargate, clientState, state, age);

        RenderSystem.disableDepthTest();

        stack.pop();
        stack.pop();

        stack.pop();
    }
}
