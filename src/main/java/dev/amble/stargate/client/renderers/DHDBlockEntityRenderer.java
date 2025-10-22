package dev.amble.stargate.client.renderers;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.address.AddressProvider;
import dev.amble.stargate.api.address.Glyph;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.client.models.DHDModel;
import dev.amble.stargate.client.util.EmissionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class DHDBlockEntityRenderer implements BlockEntityRenderer<DHDBlockEntity> {

    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd_emission.png");

    private final DHDModel model;

    private final ModelPart[] allLights;

    public DHDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DHDModel(DHDModel.getTexturedModelData().createModel());

        ModelPart[] bottomlights = new ModelPart[]{
                this.model.button6, this.model.button7, this.model.button8, this.model.button9,
                this.model.button10, this.model.button11, this.model.button12, this.model.button13, this.model.button14,
                this.model.button15, this.model.button16, this.model.button17, this.model.button18,
                this.model.button1, this.model.button2, this.model.button3, this.model.button4, this.model.button5
        };

        ModelPart[] toplights = new ModelPart[]{
                this.model.button19, this.model.button20, this.model.button21, this.model.button22,
                this.model.button23, this.model.button24, this.model.button25, this.model.button26,
                this.model.button27, this.model.button28, this.model.button29, this.model.button30,
                this.model.button31, this.model.button32, this.model.button33, this.model.button34,
                this.model.button35, this.model.button36
        };

        this.allLights = new ModelPart[bottomlights.length + toplights.length];
        System.arraycopy(bottomlights, 0, allLights, 0, bottomlights.length);
        System.arraycopy(toplights, 0, allLights, bottomlights.length, toplights.length);
    }

    // FIXME: oh my gawd bruh, this shit SUCKS ASS
    // TODO add biome overlays so the snow block renders under the DHD for fun purposes
    @Override
    public void render(DHDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (ModelPart lights : allLights) {
            lights.visible = false;
        }

        Stargate gate = entity.asGate();
        if (gate == null) return;

        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);

        float k = HorizontalBlockBehavior.getFacing(entity.getCachedState()).asRotation();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(k));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        GateState<?> state = gate.getGateState();
        boolean bl = (state instanceof GateState.Closed closed && closed.locked > 6) || state instanceof GateState.Opening || state instanceof GateState.Open;

        this.model.dialbutton.visible = !bl;
        this.model.dialbuttonlight.visible = bl;

        if (state instanceof GateState.Closed closed) {
            String address = closed.address;
            this.updateGlow(address, closed.locked);
        } else if (state instanceof GateState.Opening preOpen && preOpen.target != null) {
            String address = AddressProvider.Global.asString(preOpen.target.globalAddress());
            this.updateGlow(address, address.length() - 1);
        } else if (state instanceof GateState.Open open && open.target != null) {
            String address = AddressProvider.Global.asString(open.target.globalAddress());
            this.updateGlow(address, address.length() - 1);
        }

        renderGlyphs(matrices, vertexConsumers, entity, 0xf000f0);

        EmissionUtil.render2Layers(model, TEXTURE, EMISSION, true, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }

    private void updateGlow(String address, int len) {
        if (address.isEmpty()) return;

        for (int i = 0; i < len; i++) {
            char target = address.charAt(i);
            for (int a = 0; a < Glyph.ALL.length; a++) {
                if (Glyph.ALL[a] == target) {
                    allLights[a].visible = true;
                    break;
                }
            }
        }
    }

    private void renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, DHDBlockEntity dhd, int light) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = HorizontalBlockBehavior.getFacing(dhd.getCachedState());
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.24f * multiplier : direction.getOffsetX() * 0.24f * multiplier;

        matrices.push();
        matrices.translate(xOffset, 0.565f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);

        int length = MathHelper.ceil((float) Glyph.ALL.length / 2);

        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(65f));
        matrices.translate(0, -11.5f, 2f);

        int offset = -4; // Change this value to shift more or less

        for (int i = 0; i < length; i++) {
            matrices.push();

            int shiftedIndex = (i + offset) % length;
            double angle = 2 * Math.PI * shiftedIndex / length;

            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(0));
            matrices.scale(0.4f, 0.4f, 0.4f);
            matrices.translate(Math.sin(angle) * 18.95 * 2.35f, Math.cos(angle) * 18.95 * 2.35f, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));

            Text text = Glyph.asText(Glyph.ALL[i]);
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, 0x6f7287, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0,  light);

            matrices.pop();
        }

        offset = -9;

        for (int i = 0; i < length; i++) {
            matrices.push();
            matrices.translate(0, 0f, -2.5f);

            int shiftedIndex = (i + offset) % length;
            double angle = 2 * Math.PI * shiftedIndex / length;

            matrices.scale(0.4f, 0.4f, 0.4f);
            matrices.translate(Math.sin(angle) * 18.95 * 1.55f, Math.cos(angle) * 18.95 * 1.55f, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));

            Text text = Glyph.asText(Glyph.ALL[18 + i]);
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, 0x6f7287, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);

            matrices.pop();
        }

        matrices.pop();
    }
}