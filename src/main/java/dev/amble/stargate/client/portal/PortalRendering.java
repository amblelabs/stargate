package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.api.kernels.GateState;
import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PortalRendering {
    public static Queue<StargateBlockEntity> PORTAL_RENDER_QUEUE = new LinkedList<>();

    public static void renderPortal(StargateBlockEntity stargateBlockEntity, GateState state, MatrixStack stack) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        StargateRef ref = stargateBlockEntity.gate();

        if (!stargateBlockEntity.hasStargate() && ref.isEmpty()) return;

        boolean orlin = ref.get().kernel() instanceof OrlinGateKernel;

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        stack.translate(0, orlin ? -1f : -0.9f, 0);
        if (state instanceof GateState.PreOpen || state instanceof GateState.Open) {
            PortalUtil util = new PortalUtil("normal");

            RenderSystem.enableDepthTest();
            util.renderPortalInterior(stack, stargateBlockEntity, state);

            RenderSystem.disableDepthTest();
        }

        stack.pop();
        stack.pop();
    }
}
