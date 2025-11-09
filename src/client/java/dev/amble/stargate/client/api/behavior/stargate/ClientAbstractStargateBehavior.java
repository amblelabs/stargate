package dev.amble.stargate.client.api.behavior.stargate;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.tick.StargateTickEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.api.state.stargate.GateIdentityState;
import dev.amble.stargate.client.api.state.stargate.ClientAbstractStargateState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.api.event.render.StargateAnimateEvent;
import dev.amble.stargate.client.api.event.render.StargateRenderEvents;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.amble.stargate.client.renderers.StargateBlockEntityRenderer;
import dev.amble.stargate.client.util.EmissionUtil;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

public abstract class ClientAbstractStargateBehavior<T extends ClientAbstractStargateState<?>> implements TBehavior, StargateRenderEvents, StargateLoadedEvents, StargateTickEvents {

    private final Class<T> clazz;
    private final Class<? extends GateIdentityState> identity;

    public ClientAbstractStargateBehavior(Class<? extends GateIdentityState> identity, Class<T> clazz) {
        this.clazz = clazz;
        this.identity = identity;
    }

    protected abstract T createClientState(Stargate stargate);

    @Override
    public void onLoaded(Stargate stargate) {
        if (stargate.isClient() && identity.isInstance(stargate.state(GateIdentityState.state)))
            stargate.addState(this.createClientState(stargate));
    }

    @Override
    public void tick(Stargate stargate) {
        if (!stargate.isClient()) return;

        stargate.resolveState(ClientAbstractStargateState.state).age++;
    }

    @Override
    public void render(Stargate stargate, ClientAbstractStargateState<?> clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        this.customRender(stargate, this.tryGetState(stargate, clientState), entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);
    }

    protected T tryGetState(Stargate stargate, ClientAbstractStargateState<?> clientState) {
        // TODO: create an abstract exception class that will get caught by stuff and make StateResolveError extend it
        if (!clazz.isInstance(clientState)) throw StateResolveError.create(stargate, ClientAbstractStargateState.state);

        return (T) clientState;
    }

    protected void customRender(Stargate stargate, T clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        matrices.translate(0.5f, 1.4f, 0.5f);

        final float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        this.preRender(stargate, clientState, entity, renderer, profiler, matrices, vertexConsumers, light, overlay, tickDelta);
        boolean anyVisible = this.updateChevronVisibility(stargate, clientState.model());

        profiler.push("render2layer");
        EmissionUtil.render2Layers(clientState.model(), clientState.texture, clientState.emission, anyVisible, matrices, vertexConsumers, light, overlay);
        profiler.pop();
    }

    protected void preRender(Stargate stargate, T clientState, StargateBlockEntity entity, StargateBlockEntityRenderer renderer, Profiler profiler, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta) {
        profiler.push("animate");
        TEvents.handle(new StargateAnimateEvent(entity, stargate, clientState, profiler));
        profiler.pop();
    }

    public boolean updateChevronVisibility(Stargate stargate, BaseStargateModel model) {
        GateState<?> state = stargate.getGateState();

        boolean visible = state.gateState() != GateState.StateType.CLOSED;
        int locked = state instanceof GateState.Closed closed ? closed.locked : -1;

        ModelPart[] chevrons = model.chevrons;

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i < locked;
        }

        return visible || locked != 0;
    }

    public abstract static class Spinning<T extends ClientAbstractStargateState<?>> extends ClientAbstractStargateBehavior<T> {

        public Spinning(Class<? extends GateIdentityState> identity, Class<T> clazz) {
            super(identity, clazz);
        }

        private static final int MAX_GLYPHS = Glyph.ALL.length;
        private static final OrderedText[] GLYPHS;
        private static final float[] GLYPH_WIDTHS;

        static {
            GLYPHS = new OrderedText[MAX_GLYPHS];
            GLYPH_WIDTHS = new float[MAX_GLYPHS];

            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

            for (int i = 0; i < MAX_GLYPHS; i++) {
                OrderedText text = Glyph.asText(Glyph.ALL[i]).asOrderedText();

                GLYPHS[i] = text;
                GLYPH_WIDTHS[i] = renderer.getWidth(text) / -2f;
            }
        }

        public static boolean shouldRenderGlyphs(MinecraftClient client, Stargate stargate) {
            Vec3d pos = client.gameRenderer.getCamera().getPos();
            return Vec3d.ofCenter(stargate.pos()).isInRange(pos, 16);
        }

        public static float renderGlyphs(float rot, final float initialRot, final ClientAbstractStargateState<?> glyphState, final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Stargate gate, final int light) {
            final MinecraftClient client = MinecraftClient.getInstance();
            rot = rot * 180 / MathHelper.PI;

            if (shouldRenderGlyphs(client, gate)) {
                final TextRenderer renderer = client.textRenderer;
                final Direction direction = gate.facing();

                final boolean northern = direction.getAxis() == Direction.Axis.Z;
                final int multiplier = -direction.getDirection().offset();

                final float xOffset = (northern ? direction.getOffsetX() : direction.getOffsetZ()) * 0.3f * multiplier;
                final float zOffset = (northern ? direction.getOffsetZ() : direction.getOffsetX()) * 0.24f * multiplier;

                matrices.push();
                matrices.translate(0, -2.05f, 0);
                matrices.translate(xOffset, 0.05f, zOffset);
                matrices.scale(0.025f, 0.025f, 0.025f);

                final GateState.Closed closed = gate.stateOrNull(GateState.Closed.state);
                final int lockingChevronIdx = closed != null && (closed.locked > 0 || closed.locking) ? closed.locked : -1; // TODO: in theory, closed.locked > 0 check is redundant
                final int selectedGlyphIdx = closed != null && lockingChevronIdx != -1 && lockingChevronIdx < closed.address.length() ? closed.glyphIdxAtChevron(lockingChevronIdx) : -1;

                if (closed != null && closed.locking) {
                    float rotProgress = (float) closed.timer / (float) GateState.Closed.TICKS_PER_GLYPH2;

                    // FIXME: make this calculation be done in rad instead of deg
                    float selectedRot = MathHelper.wrapDegrees(initialRot + 360f * selectedGlyphIdx / GLYPHS.length);

                    System.out.println("rot: " + rot + "/" + selectedRot + "; " + rotProgress * 100 + "%");
                    rot += MathHelper.wrapDegrees(selectedRot * rotProgress - rot)
                            + (360f * (closed.locked % 2 == 0 ? -1 : 1));
                }

                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));

                for (int i = 0; i < MAX_GLYPHS; i++) {
                    final boolean isInDial = closed != null && closed.address$contains(Glyph.ALL[i]);
                    final boolean isSelected = isInDial && i == selectedGlyphIdx;

                    final float angle = 2 * MathHelper.PI * i / MAX_GLYPHS;
                    final int color = isInDial ? 0x5c5c73 : glyphState.glyphColor;
                    final int glyphLight = isSelected ? 0xf000f0 : light;

                    matrices.push();

                    matrices.translate(MathHelper.sin(angle) * 117, MathHelper.cos(angle) * 117, 0);

                    // rotate the symbols towards the gate
                    matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (initialRot + Math.toDegrees(angle))));

                    renderer.draw(GLYPHS[i], GLYPH_WIDTHS[i], -4, color, false,
                            matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, glyphLight);

                    matrices.pop();
                }

                matrices.pop();
            }

            return rot * MathHelper.PI / 180f;
        }
    }
}
