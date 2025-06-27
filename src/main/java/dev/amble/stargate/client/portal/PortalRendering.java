package dev.amble.stargate.client.portal;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.Queue;

public class PortalRendering {
    public static Queue<StargateBlockEntity> PORTAL_RENDER_QUEUE = new LinkedList<>();

    public static void renderPortal(StargateBlockEntity stargateBlockEntity, Stargate.GateState state, MatrixStack stack) {
        if (MinecraftClient.getInstance().world == null
                || MinecraftClient.getInstance().player == null) return;

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

        stack.push();
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        stack.translate(0, -1f, 0);
        PortalUtil util = new PortalUtil("watery");
        if (state == Stargate.GateState.PREOPEN || state == Stargate.GateState.OPEN) {
            RenderSystem.enableDepthTest();
            util.renderPortalInterior(stack, stargateBlockEntity.getGateState());
            if (state == Stargate.GateState.PREOPEN)
                util.triggerCentralRipple(0.055f, 0.175f, 0.01f, 0.2f);
            RenderSystem.disableDepthTest();
        }
        stack.pop();


        stack.pop();
    }
}
