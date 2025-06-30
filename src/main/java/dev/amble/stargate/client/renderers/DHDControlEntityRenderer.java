package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.Address;
import dev.amble.stargate.client.models.ControlModel;
import dev.amble.stargate.entities.DHDControlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

@Environment(value = EnvType.CLIENT)
public class DHDControlEntityRenderer extends LivingEntityRenderer<DHDControlEntity, ControlModel> {

    public DHDControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ControlModel(ControlModel.getNotModelData().createModel()), 0f);
    }

    @Override
    public void render(DHDControlEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int light) {
        super.render(livingEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    protected void renderLabelIfPresent(DHDControlEntity entity, Text text, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, int light) {
    }

    @Override
    public Identifier getTexture(DHDControlEntity controlEntity) {
        return TextureManager.MISSING_IDENTIFIER;
    }

}
