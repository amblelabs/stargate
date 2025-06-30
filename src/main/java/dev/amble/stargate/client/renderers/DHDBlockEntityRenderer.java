package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.client.models.DHDModel;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.*;

public class DHDBlockEntityRenderer implements BlockEntityRenderer<DHDBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd_emission.png");
    private final DHDModel model;
    private final ModelPart[] bottomlights;
    private final ModelPart[] toplights;

    public DHDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DHDModel(DHDModel.getTexturedModelData().createModel());
        this.bottomlights = new ModelPart[] {
                this.model.button1, this.model.button2, this.model.button3, this.model.button4, this.model.button5,
                this.model.button6, this.model.button7, this.model.button8, this.model.button9, this.model.button10,
                this.model.button11, this.model.button12, this.model.button13, this.model.button14, this.model.button15,
                this.model.button16, this.model.button17, this.model.button18, this.model.button19
        };
        this.toplights = new ModelPart[] {
                this.model.button20, this.model.button21, this.model.button22, this.model.button23, this.model.button24,
                this.model.button25, this.model.button26, this.model.button27, this.model.button28, this.model.button29,
                this.model.button30, this.model.button31, this.model.button32, this.model.button33, this.model.button34,
                this.model.button35, this.model.button36, this.model.button37, this.model.button38
        };
    }
    @Override
    public void render(DHDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {


        // TODO add a snow state so the snow block renders under the DHD for fun purposes
        if (entity.getWorld().getBlockState(entity.getPos().north()).getBlock() instanceof SnowBlock) {
            BlockState snowState = Blocks.SNOW.getDefaultState();
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            matrices.translate(-1, 0, -1);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(snowState, entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayer.getBlockLayers().get(0)), false, MinecraftClient.getInstance().world.getRandom());
            matrices.pop();
        }

        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        /*this.model.toplights.visible = true;
        this.model.bottomlights.visible = true;
        this.model.dialbuttonlight.visible = true;*/
        // Animate the lights in a circle: only one is active at a time, based on world time
        List<ModelPart> allLights = new ArrayList<>();
        allLights.addAll(Arrays.asList(this.bottomlights));
        allLights.addAll(Arrays.asList(this.toplights));

        // Use a persistent random seed based on world time, changing every 3 seconds
        long seed = MinecraftClient.getInstance().world.getTime() / 60; // 20 ticks per second * 3 = 60
        Collections.shuffle(allLights, new Random(seed));

        // Hide all lights
        for (ModelPart lights : allLights) {
            lights.visible = false;
        }

        // Show 7 random lights
        for (int i = 0; i < Math.min(7, allLights.size()); i++) {
            allLights.get(i).visible = true;
        }
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}