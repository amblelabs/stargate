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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {

    public static final OrlinGateModel ORLIN_GATE = new OrlinGateModel(OrlinGateModel.getTexturedModelData().createModel());

    public final StargateModel model = new StargateModel(StargateModel.getTexturedModelData().createModel());

    public final ModelPart[] chevrons = new ModelPart[] {
            model.chev_light, model.chev_light2, model.chev_light3, model.chev_light4,
            model.chev_light5, model.chev_light6, model.chev_light7, model.chev_light7bottom
    };

    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) { }

    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Profiler profiler = MinecraftClient.getInstance().getProfiler();
        profiler.push("stargate");

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

        TEvents.handle(new StargateRenderEvent(gate, entity, this, profiler, matrices, vertexConsumers, light, overlay, tickDelta));

        matrices.pop();

        if (gate.getGateState().gateState() != GateState.StateType.CLOSED) PortalRendering.QUEUE.add(entity);

        profiler.pop();
    }

    private static final OrderedText[] GLYPHS;
    private static final float[] GLYPH_WIDTHS;

    static {
        GLYPHS = new OrderedText[Glyph.ALL.length];
        GLYPH_WIDTHS = new float[Glyph.ALL.length];

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        for (int i = 0; i < GLYPHS.length; i++) {
            OrderedText text = Glyph.asText(Glyph.ALL[i]).asOrderedText();

            GLYPHS[i] = text;
            GLYPH_WIDTHS[i] = renderer.getWidth(text) / -2f;
        }
    }

    private boolean shouldRenderGlyphs(MinecraftClient client, Stargate stargate) {
        Vec3d pos = client.gameRenderer.getCamera().getPos();
        return Vec3d.ofCenter(stargate.pos()).isInRange(pos, 16);
    }

    public float renderGlyphs(ClientAbstractStargateState glyphState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate, int light, int age) {
        final MinecraftClient client = MinecraftClient.getInstance();

        if (shouldRenderGlyphs(client, gate)) {
            TextRenderer renderer = client.textRenderer;
            Direction direction = gate.facing();

            final boolean northern = direction.getAxis() == Direction.Axis.Z;
            final int multiplier = -direction.getDirection().offset();

            final float xOffset = (northern ? direction.getOffsetX() : direction.getOffsetZ()) * 0.3f * multiplier;
            final float zOffset = (northern ? direction.getOffsetZ() : direction.getOffsetX()) * 0.24f * multiplier;

            matrices.push();
            matrices.translate(0, -2.05f, 0);
            matrices.translate(xOffset, 0.05f, zOffset);
            matrices.scale(0.025f, 0.025f, 0.025f);

            final GateState.Closed closed = gate.stateOrNull(GateState.Closed.state);
            final int selectedIndex = closed != null && (closed.locked > 0 || closed.locking) ? closed.locked : -1;

            final float baseAngle = 2 * MathHelper.PI / GLYPHS.length;

            for (int i = 0; i < GLYPHS.length; i++) {
                final boolean isInDial = closed != null && closed.address.indexOf(Glyph.ALL[i]) != -1; // FIXME: #contains on address
                final boolean isSelected = i == selectedIndex;

                final float angle = baseAngle * i;
                final int color = isInDial ? 0x5c5c73 : glyphState.glyphColor;
                final int glyphLight = isSelected ? 0xf000f0 : light;

                matrices.push();

                matrices.translate(MathHelper.sin(angle) * 117, MathHelper.cos(angle) * 117, 0);
                matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));

                renderer.draw(GLYPHS[i], GLYPH_WIDTHS[i], -4, color, false,
                        matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, glyphLight);

                matrices.pop();
            }

            matrices.pop();
        }

        return (float) MathHelper.wrapDegrees(age / 200f * Math.PI * 2);
    }
}