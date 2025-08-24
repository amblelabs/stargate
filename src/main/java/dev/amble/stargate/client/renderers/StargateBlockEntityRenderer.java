package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.kernels.GateState;
import dev.amble.stargate.api.kernels.StargateKernel;
import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.AbstractStargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.client.models.OrlinGateModel;
import dev.amble.stargate.client.models.StargateModel;
import dev.amble.stargate.client.portal.PortalRendering;
import dev.amble.stargate.compat.DependencyChecker;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
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
        if (entity.getBlockSet() != null) {
            BlockState blockState = entity.getBlockSet();
            matrices.push();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(blockState, entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(blockState)), true, MinecraftClient.getInstance().world.getRandom());
            matrices.pop();
        }

        float k = entity.getCachedState().get(AbstractStargateBlock.FACING).asRotation();

        matrices.push();
        GateState state = entity.hasStargate() ? entity.gate().get().state() : FALLBACK;

        matrices.translate(0.5f, entity.hasStargate() && entity.gate().get().kernel() instanceof OrlinGateKernel ? 1.5f : 1.4f, 0.5f);

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
                if (DependencyChecker.hasIris()) {
                    if (!(state instanceof GateState.Closed)) ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, power, power, 1);
                }
                ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), lightAbove, overlay, 1, 1, 1, 1);
                if (!DependencyChecker.hasIris()) {
                    if (!(state instanceof GateState.Closed)) ORLIN_GATE.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, power, power, 1);
                }
                matrices.pop();
                PortalRendering.PORTAL_RENDER_QUEUE.add(entity);
                return;
            }

            this.setFromDialer(state, gate.kernel());
            float rotationValue = this.renderGlyphs(matrices, vertexConsumers, gate, lightAbove);

            boolean bl = (state instanceof GateState.Closed closed && closed.locked() >= 7) || state instanceof GateState.PreOpen|| state instanceof GateState.Open;

            this.model.chev_light7.visible = bl;
            this.model.chev_light7bottom.visible = bl;
            rot = rotationValue;
        }

        this.model.animateStargateModel(entity, state, entity.age);
        this.model.SymbolRing.roll = rot;
        if(this.model.getChild("iris").isPresent()) this.model.iris.visible = entity.IRIS_CLOSE_STATE.isRunning() || entity.IRIS_OPEN_STATE.isRunning();
        if (DependencyChecker.hasIris()) {
            this.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, power, power, 1);
        }
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), lightAbove, overlay, 1, 1, 1, 1);
        if (!DependencyChecker.hasIris()) {
            this.model.render(matrices, vertexConsumers.getBuffer(StargateRenderLayers.emissiveCullZOffset(emission, true)), 0xF000F0, overlay, 1, power, power, 1);
        }
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