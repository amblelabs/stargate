package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.core.block.StargateBlock;
import dev.amble.stargate.core.block.entities.StargateBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
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
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way_emission.png");
    private final StargateModel model;

    private final GateState.Closed FALLBACK = new GateState.Closed();

    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new StargateModel(StargateModel.getTexturedModelData().createModel());
    }
    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        GateState state = entity.hasStargate() ? entity.gate().get().state() : FALLBACK;

        matrices.translate(0.5f, 1.5f, 0.5f);
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(1, 1, 1);

        float power = 1;

        // god this is ridiculous.
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up().up().up().up());

        float rot = 0;

        if (entity.hasStargate()) {
            Stargate gate = entity.gate().get();
            this.setFromDialer(state);
            float rotationValue = this.renderGlyphs(matrices, vertexConsumers, gate, lightAbove);

            power = Math.min(gate.energy() / gate.maxEnergy(), 1);

            boolean bl = (state instanceof GateState.Closed closed && closed.locking())
                         || state instanceof GateState.Open || state instanceof GateState.PreOpen;

            this.model.chev_light7.visible = bl;
            this.model.chev_light7bottom.visible = bl;
            rot = rotationValue;
        }

        this.model.animateStargateModel(entity, state, entity.age);
        this.model.SymbolRing.roll = rot;
        this.model.iris.visible = entity.IRIS_CLOSE_STATE.isRunning() || entity.IRIS_OPEN_STATE.isRunning();
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), lightAbove, overlay, 1, 1, 1, 1);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(EMISSION)), 0xF000F0, overlay, 1, power, power, 1);
        matrices.pop();

        PortalRendering.PORTAL_RENDER_QUEUE.add(entity);
    }

    private void setFromDialer(GateState state) {
        model.chev_light8.visible = false;
        model.chev_light9.visible = false;

        boolean visible = state instanceof GateState.Open || state instanceof GateState.PreOpen;
        int locked = state instanceof GateState.Closed closed ? closed.locked() : -1;

        // FIXME: this should be done at the top level of the class,
        //  not in a method that gets ran every time ffs.
        ModelPart[] chevrons = new ModelPart[] {
                model.chev_light, model.chev_light2, model.chev_light3, model.chev_light4,
                model.chev_light5, model.chev_light6, model.chev_light7, model.chev_light7bottom
        };

        for (int i = 0; i < chevrons.length; i++) {
            chevrons[i].visible = visible || i <= locked;
        }
    }

    private float renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate, int light) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = gate.address().pos().getRotationDirection();
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.24f * multiplier : direction.getOffsetX() * 0.24f * multiplier;

        matrices.push();
        matrices.translate(0, -2.05f, 0);
        matrices.translate(xOffset, 0.05f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);

        GateState state = gate.state();

        // TODO fix the rotation stuff here. - Loqor
        int middleIndex = Glyph.ALL.length / 2;
        int selectedIndex = state instanceof GateState.Closed closed ? closed.locked() : -1;
        float selectedRot = 180 + (float) (18.5f * (0.5 * selectedIndex));
        float rot = selectedIndex > -1 ? selectedRot :
                MathHelper.wrapDegrees(MinecraftClient.getInstance().player.age / 100f * 360f);

        boolean isDialing = state instanceof GateState.Closed closed && closed.isDialing();

        //if (isDialing)
        //    rot = rot + (dialer.getRotation().equals(Dialer.Rotation.FORWARD) ? 9 : -9) * dialer.getRotationProgress();

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));

        for (int i = 0; i < Glyph.ALL.length; i++) {
            boolean isInDial = state instanceof GateState.Closed closed && closed.contains(Glyph.ALL[i]);
            boolean isSelected = i == selectedIndex;

            int colour = 0x17171b;

            if (isInDial) {
                colour = 0x17171b;
            }
            if (isSelected && isDialing) {
                colour = 0x17171b;
            }

            matrices.push();
            double angle = 2 * Math.PI * i / Glyph.ALL.length;
            matrices.translate(Math.sin(angle) * 117, Math.cos(angle) * 117, 0);
            // TODO fix the rotation stuff here. - Loqor
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(rot));
            OrderedText text = Address.asText(String.valueOf(Glyph.ALL[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, isSelected ? 0xf000f0 : light);
            matrices.pop();
        }
        matrices.pop();
        return rot;
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
}