package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.LinkedList;
import java.util.Queue;

public class PortalRendering {
    public static Queue<StargateBlockEntity> PORTAL_RENDER_QUEUE = new LinkedList<>();

    public static void renderPortal(StargateBlockEntity stargateBlockEntity, GateState state, MatrixStack stack) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        stack.translate(0, -1f, 0);
        PortalUtil util = new PortalUtil("watery");
        //if (state instanceof GateState.PreOpen || state instanceof GateState.Open) {
            RenderSystem.enableDepthTest();
            util.renderPortalInterior(stack, stargateBlockEntity, state);

            if (state instanceof GateState.PreOpen)
                util.triggerCentralRipple(0.055f, 0.175f, 0.01f, 0.2f);

            RenderSystem.disableDepthTest();
        //}
        stack.pop();


        stack.pop();
    }
}
