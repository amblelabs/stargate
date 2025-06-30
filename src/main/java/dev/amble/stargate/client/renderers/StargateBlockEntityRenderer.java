package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.api.v2.*;
import dev.amble.stargate.client.models.BaseStargateModel;
import dev.amble.stargate.client.models.OrlinGateModel;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
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
    public static final Identifier MILKY_WAY = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way.png");
    public static final Identifier MILKY_WAY_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way_emission.png");
    public static final Identifier PEGASUS = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/pegasus/pegasus.png");
    public static final Identifier PEGASUS_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/pegasus/pegasus_emission.png");
    public static final Identifier DESTINY = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/destiny/destiny.png");
    public static final Identifier DESTINY_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/destiny/destiny_emission.png");
    public static final Identifier ORLIN = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/orlin/orlin.png");
    public static final Identifier ORLIN_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/orlin/orlin_emission.png");
    private StargateModel model;
    private static final OrlinGateModel ORLIN_GATE = new OrlinGateModel(OrlinGateModel.getTexturedModelData().createModel());

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
        Identifier texture = MILKY_WAY;
        Identifier emission = MILKY_WAY_EMISSION;

        if (entity.hasStargate()) {
            Stargate gate = entity.gate().get();
            texture = getTextureForGate(gate);
            emission = getEmissionForGate(gate);
            if (gate.kernel instanceof OrlinGateKernel) {
                ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), lightAbove, overlay, 1, 1, 1, 1);
                ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(emission)), 0xF000F0, overlay, 1, power, power, 1);
                matrices.pop();
                PortalRendering.PORTAL_RENDER_QUEUE.add(entity);
                return;
            }
            this.setFromDialer(state, gate.kernel);
            float rotationValue = this.renderGlyphs(matrices, vertexConsumers, gate, lightAbove);

            power = 1;//Math.min(gate.energy() / gate.maxEnergy(), 1);

            boolean bl = (state instanceof GateState.Closed closed && closed.locking())
                         || state instanceof GateState.Open || state instanceof GateState.PreOpen;

            this.model.chev_light7.visible = bl;
            this.model.chev_light7bottom.visible = bl;
            rot = rotationValue;
        }

        this.model.animateStargateModel(entity, state, entity.age);
        this.model.SymbolRing.roll = rot;
        if(this.model.getChild("iris").isPresent()) this.model.iris.visible = entity.IRIS_CLOSE_STATE.isRunning() || entity.IRIS_OPEN_STATE.isRunning();
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), lightAbove, overlay, 1, 1, 1, 1);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(emission)), 0xF000F0, overlay, 1, power, power, 1);
        matrices.pop();

        PortalRendering.PORTAL_RENDER_QUEUE.add(entity);
    }

    private void setFromDialer(GateState state, StargateKernel.Impl kernel) {
        model.chev_light8.visible = false;
        model.chev_light9.visible = false;

        boolean visible = state instanceof GateState.Open || state instanceof GateState.PreOpen;
        int locked = (state instanceof GateState.Closed closed) ? closed.locked() : -1;

        // FIXME: this should be done at the top level of the class,
        //  not in a method that gets ran every time ffs.
        ModelPart[] chevrons = new ModelPart[] {
                model.chev_light, model.chev_light2, model.chev_light3, model.chev_light4,
                model.chev_light5, model.chev_light6, model.chev_light7, model.chev_light7bottom
        };

        if (kernel instanceof MilkyWayGateKernel) {
            for (int i = 0; i < chevrons.length; i++) {
                chevrons[i].visible = visible || i < locked;
            }
        } else {
            for (ModelPart chevron : chevrons) {
                chevron.visible = visible;
            }
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

        int selectedIndex = state instanceof GateState.Closed closed ? closed.locked() : -1;
        float baseSpeed = 360f / Glyph.ALL.length; // degrees per glyph
        float time = MinecraftClient.getInstance().player.age / 200f;
        float rot = 0;
        boolean isDialing = state instanceof GateState.Closed closed && closed.isDialing();

        if (isDialing)
            rot = MathHelper.wrapDegrees(time * baseSpeed * Glyph.ALL.length);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));

        for (int i = 0; i < Glyph.ALL.length; i++) {
            boolean isInDial = state instanceof GateState.Closed closed && closed.contains(Glyph.ALL[i]);
            boolean isSelected = i == selectedIndex;

            int colour = gate.kernel instanceof PegasusGateKernel ? 0xffffff : 0x17171b;

            if (isInDial) {
                colour = 0x17171b;
            }
            if (isSelected && isDialing) {
                colour = 0x17171b;
            }

            matrices.push();
            double angle = 2 * Math.PI * i / Glyph.ALL.length;
            matrices.translate(Math.sin(angle) * 117, Math.cos(angle) * 117, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));
            OrderedText text = Address.asText(String.valueOf(Glyph.ALL[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, isSelected ? 0xf000f0 : light);
            matrices.pop();
        }
        matrices.pop();
        return !isDialing ? 0 : (float) MathHelper.wrapDegrees(time * (Math.PI * 2 / Glyph.ALL.length) * Glyph.ALL.length);
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

    public Identifier getTextureForGate(Stargate gate) {
        StargateKernel.Impl impl = gate.kernel;
        if (impl instanceof PegasusGateKernel) {
            return PEGASUS;
        }
        if (impl instanceof DestinyGateKernel) {
            return DESTINY;
        }
        if (impl instanceof OrlinGateKernel) {
            return ORLIN;
        }
        return MILKY_WAY;
    }

    public Identifier getEmissionForGate(Stargate gate) {
        StargateKernel.Impl impl = gate.kernel;
        if (impl instanceof PegasusGateKernel) {
            return PEGASUS_EMISSION;
        }
        if (impl instanceof DestinyGateKernel) {
            return DESTINY_EMISSION;
        }
        if (impl instanceof OrlinGateKernel) {
            return ORLIN_EMISSION;
        }
        return MILKY_WAY_EMISSION;
    }
}