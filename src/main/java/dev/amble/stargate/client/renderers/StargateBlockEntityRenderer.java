package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.v2.GateState;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.api.v2.StargateKernel;
import dev.amble.stargate.api.v2.kernels.OrlinGateKernel;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.OrlinGateModel;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class StargateBlockEntityRenderer implements BlockEntityRenderer<StargateBlockEntity> {
    public static final Identifier MILKY_WAY = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way.png");
    public static final Identifier MILKY_WAY_EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/stargates/milky_way/milky_way_emission.png");
    private final StargateModel model = new StargateModel(StargateModel.getTexturedModelData().createModel());
    private static final OrlinGateModel ORLIN_GATE = new OrlinGateModel(OrlinGateModel.getTexturedModelData().createModel());
    private final GlyphRenderer glyphRenderer = new GlyphRenderer();

    private final GateState.Closed FALLBACK = new GateState.Closed();

    public StargateBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    private final ChevronVisibilityHelper chevronVisibilityHelper = new ChevronVisibilityHelper(this.model);

    @Override
    public void render(StargateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float k = entity.getCachedState().get(StargateBlock.FACING).asRotation();
        /*Direction facing = entity.getCachedState().get(StargateBlock.FACING);
        Box northSouthBox = new Box(entity.getPos()).expand(2, 2, 0).offset(0, 3, 0);
        Box westEastBox = new Box(entity.getPos()).expand(0, 2, 2).offset(0, 3, 0);;
        Box box = switch (facing) {
            case WEST, EAST  -> westEastBox;
            default -> northSouthBox;
        };
        boolean colorRed = box.intersects(MinecraftClient.getInstance().player.getBoundingBox());
        matrices.push();
        matrices.translate(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
        DebugRenderer.drawBox(
                matrices,
                vertexConsumers,
                box ,1, colorRed ? 0 : 1, colorRed ? 0 : 1, 0.1f
        );
        matrices.pop();*/

        matrices.push();
        GateState state = entity.hasStargate() ? entity.gate().get().state() : FALLBACK;

        matrices.translate(0.5f, 1.5f, 0.5f);

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
            if (gate.kernel() instanceof OrlinGateKernel) {
                ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), lightAbove, overlay, 1, 1, 1, 1);
                ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEyes(emission)), 0xF000F0, overlay, 1, power, power, 1);
                matrices.pop();
                PortalRendering.PORTAL_RENDER_QUEUE.add(entity);
                return;
            }

            this.setFromDialer(state, gate.kernel());
            float rotationValue = this.renderGlyphs(matrices, vertexConsumers, gate, lightAbove);

            boolean bl = (entity.CHEVRON_LOCK_STATE.isRunning() && ((state instanceof GateState.Closed closed))
                         || state instanceof GateState.Open || state instanceof GateState.PreOpen);

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
        chevronVisibilityHelper.setFromDialer(state, kernel);
    }

    private float renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Stargate gate, int light) {
        return glyphRenderer.renderGlyphs(matrices, vertexConsumers, gate, light);
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
        return StargateTextureUtil.getTextureForGate(gate);
    }

    public Identifier getEmissionForGate(Stargate gate) {
        return StargateTextureUtil.getEmissionForGate(gate);
    }
}