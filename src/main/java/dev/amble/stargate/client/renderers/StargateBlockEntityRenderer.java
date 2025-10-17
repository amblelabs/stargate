package dev.amble.stargate.client.renderers;

import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.render.StargateRenderEvent;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.client.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.OrlinGateModel;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {

    public final StargateModel model = new StargateModel(StargateModel.getTexturedModelData().createModel());

    public final ModelPart[] chevrons = new ModelPart[] {
            model.chev_light, model.chev_light2, model.chev_light3, model.chev_light4,
            model.chev_light5, model.chev_light6, model.chev_light7, model.chev_light7bottom
    };

    public static final OrlinGateModel ORLIN_GATE = new OrlinGateModel(OrlinGateModel.getTexturedModelData().createModel());

    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getBlockSet() != null) {
            matrices.push();

            BlockState blockState = entity.getBlockSet();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(blockState,
                    entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(
                            RenderLayers.getBlockLayer(blockState)), true, entity.getWorld().random
            );

            matrices.pop();
        }

        Stargate gate = entity.asGate();
        if (gate == null) return;

        matrices.push();

        TEvents.handle(new StargateRenderEvent(gate, entity, this, matrices, vertexConsumers, light, overlay, tickDelta));

        matrices.pop();

        if (gate.getGateState().gateState() != GateState.StateType.CLOSED)
            PortalRendering.QUEUE.add(entity);
    }

    @Override
    public boolean rendersOutsideBoundingBox(StargateBlockEntity stargateBlockEntity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public boolean isInRenderDistance(StargateBlockEntity exteriorBlockEntity, Vec3d vec3d) {
        return Vec3d.ofCenter(exteriorBlockEntity.getPos()).multiply(1.0, 0.0, 1.0).isInRange(vec3d.multiply(1.0, 0.0, 1.0), this.getRenderDistance());
    }

    public float renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate, int light, int age) {
        ClientAbstractStargateState glyphState = gate.stateOrNull(ClientAbstractStargateState.state);

        if (glyphState == null) return 0;

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = gate.facing();
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.24f * multiplier : direction.getOffsetX() * 0.24f * multiplier;

        matrices.push();
        matrices.translate(0, -2.05f, 0);
        matrices.translate(xOffset, 0.05f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);

        GateState.Closed closed = gate.stateOrNull(GateState.Closed.state);
        int selectedIndex = closed != null ? closed.locked : -1;

        for (int i = 0; i < Glyph.ALL.length; i++) {
            boolean isInDial = closed != null && closed.address.contains(Glyph.ALL[i] + "");
            boolean isSelected = i == selectedIndex;

            int color = isInDial ? 0x5c5c73 : glyphState.glyphColor;

            matrices.push();
            double angle = 2 * Math.PI * i / Glyph.ALL.length;
            matrices.translate(Math.sin(angle) * 117, Math.cos(angle) * 117, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));
            OrderedText text = Glyph.asText(Glyph.ALL[i]).asOrderedText();

            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, color, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, isSelected ? 0xf000f0 : light);

            matrices.pop();
        }

        matrices.pop();

        return (float) MathHelper.wrapDegrees(age / 200f * (Math.PI * 2 / Glyph.ALL.length) * Glyph.ALL.length);
    }

    public static Identifier getTextureForGate(Stargate gate) {
        return gate.kernel().id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + ".png");
    }

    public static Identifier getEmissionForGate(Stargate gate) {
        return gate.kernel().id.withPath(s -> "textures/blockentities/stargates/" + s + "/" + s + "_emission.png");
    }
}