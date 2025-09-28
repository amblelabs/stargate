package dev.amble.stargate.client.renderers;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.Address;
import dev.amble.stargate.api.Glyph;
import dev.amble.stargate.block.DHDBlock;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.DHDBlockEntity;
import dev.amble.stargate.client.models.DHDModel;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DHDBlockEntityRenderer implements BlockEntityRenderer<DHDBlockEntity> {
    public static final Identifier TEXTURE = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd.png");
    public static final Identifier EMISSION = new Identifier(StargateMod.MOD_ID, "textures/blockentities/dhd_emission.png");
    private final DHDModel model;
    private final ModelPart[] bottomlights;
    private final ModelPart[] toplights;

    public DHDBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DHDModel(DHDModel.getTexturedModelData().createModel());
        this.bottomlights = new ModelPart[] {
                this.model.button6, this.model.button7, this.model.button8, this.model.button9,
                this.model.button10, this.model.button11, this.model.button12, this.model.button13, this.model.button14,
                this.model.button15, this.model.button16, this.model.button17, this.model.button18,
                this.model.button1, this.model.button2, this.model.button3, this.model.button4, this.model.button5
        };
        this.toplights = new ModelPart[] {
                this.model.button19, this.model.button20, this.model.button21, this.model.button22,
                this.model.button23, this.model.button24, this.model.button25, this.model.button26,
                this.model.button27, this.model.button28, this.model.button29, this.model.button30,
                this.model.button31, this.model.button32, this.model.button33, this.model.button34,
                this.model.button35, this.model.button36
        };
    }
    @Override
    public void render(DHDBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
                // TODO add a snow state so the snow block renders under the DHD for fun purposes
        if (entity == null) return;
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
        this.model.bottomlights.visible = true;*/
        List<ModelPart> allLights = new ArrayList<>();
        allLights.addAll(Arrays.asList(this.bottomlights));
        allLights.addAll(Arrays.asList(this.toplights));

        long seed = MinecraftClient.getInstance().world.getTime() / 60;

        for (ModelPart lights : allLights) {
            lights.visible = false;
        }

        if (entity.hasStargate()) {
            var gate = entity.gate().get();
//            GateState state = gate.kernel().state();
//            boolean bl = (state instanceof GateState.Closed closed && closed.locked() > 6 && closed.hasDialButton()) || state instanceof GateState.PreOpen || state instanceof GateState.Open;
//            this.model.dialbutton.visible = !bl;
//            this.model.dialbuttonlight.visible = bl;
//
//
//
//            if (state instanceof GateState.Closed closed) {
//                String addressText = closed.addressBuilder();
//                if (!addressText.isEmpty()) {
//                    for (int a = 0; a < closed.locked(); a++) {
//                        char target = addressText.charAt(a);
//                        for (int i = 0; i < Glyph.ALL.length; i++) {
//                            if (Glyph.ALL[i] == target) {
//                                allLights.get(i).visible = true;
//                                break;
//                            }
//                        }
//                    }
//                }
//            } else if (state instanceof GateState.PreOpen preOpen) {
//                String address = preOpen.address();
//                for (int i = 0; i < address.length() - 1; i++) {
//                    char target = address.charAt(i);
//                    for (int a = 0; a < Glyph.ALL.length; a++) {
//                        if (Glyph.ALL[a] == target) {
//                            allLights.get(a).visible = true;
//                            break;
//                        }
//                    }
//                }
//            } else if (state instanceof GateState.Open open) {
//                String address = open.target().get().address().text();
//                for (int i = 0; i < address.length() - 1; i++) {
//                    char target = address.charAt(i);
//                    for (int a = 0; a < Glyph.ALL.length; a++) {
//                        if (Glyph.ALL[a] == target) {
//                            allLights.get(a).visible = true;
//                            break;
//                        }
//                    }
//                }
//            }
        }

        renderGlyphs(matrices, vertexConsumers, entity, 0xf000f0);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(EMISSION)), 0xF000F0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }

    private float renderGlyphs(MatrixStack matrices, VertexConsumerProvider vertexConsumers, DHDBlockEntity dhd, int light) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        Direction direction = dhd.getCachedState().get(DHDBlock.FACING);
        boolean northern = direction == Direction.NORTH || direction == Direction.SOUTH;
        int multiplier = (direction == Direction.WEST || direction == Direction.NORTH) ? 1 : -1;
        float xOffset = northern ? direction.getOffsetX() * 0.3f * multiplier : direction.getOffsetZ() * 0.3f * multiplier;
        float zOffset = northern ? direction.getOffsetZ() * 0.24f * multiplier : direction.getOffsetX() * 0.24f * multiplier;

        matrices.push();
        matrices.translate(xOffset, 0.565f, zOffset);
        matrices.scale(0.025f, 0.025f, 0.025f);

        //GateState state = gate.state();

        float length = (float) Glyph.ALL.length / 2;

        int selectedIndex = 0;//state instanceof GateState.Closed closed ? closed.locked() : -1;
        boolean isDialing = false;//state instanceof GateState.Closed closed && closed.isDialing();
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(65f));
        matrices.translate(0, -11.5f, 2f);

        for (int i = 0; i < length; i++) {
            boolean isInDial = false;//state instanceof GateState.Closed closed && closed.contains(Glyph.ALL[i]);
            boolean isSelected = i == selectedIndex;

            int colour = /*gate.kernel instanceof PegasusGateKernel ? 0xffffff : */0x6f7287;

            if (isInDial) {
                colour = 0x6f7287;
            }
            if (isSelected && isDialing) {
                colour = 0x6f7287;
            }

            matrices.push();
            // Shift the glyph positions by an offset (e.g., 5 positions)
            int offset = -4; // Change this value to shift more or less
            int shiftedIndex = (i + offset) % (int)length;
            double angle = 2 * Math.PI * shiftedIndex / length;
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(0));
            matrices.scale(0.4f, 0.4f, 0.4f);
            matrices.translate(Math.sin(angle) * 18.95 * 2.35f, Math.cos(angle) * 18.95 * 2.35f, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));
            OrderedText text = /*Text.literal(String.valueOf(Glyph.ALL[i]));*/Address.asText(String.valueOf(Glyph.ALL[i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, 0x6f7287, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0,  light);
            matrices.pop();
        }

        for (int i = 0; i < length; i++) {
            boolean isInDial = false;//state instanceof GateState.Closed closed && closed.contains(Glyph.ALL[i]);
            boolean isSelected = i == selectedIndex;

            int colour = /*gate.kernel instanceof PegasusGateKernel ? 0xffffff : */0x6f7287;

            if (isInDial) {
                colour = 0x6f7287;
            }
            if (isSelected && isDialing) {
                colour = 0x6f7287;
            }

            matrices.push();
            matrices.translate(0, 0f, -2.5f);
            int offset = -9; // Change this value to shift more or less
            int shiftedIndex = (i + offset) % (int)length;
            double angle = 2 * Math.PI * shiftedIndex / length;
            matrices.scale(0.4f, 0.4f, 0.4f);
            matrices.translate(Math.sin(angle) * 18.95 * 1.55f, Math.cos(angle) * 18.95 * 1.55f, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((float) (180f + Math.toDegrees(angle))));
            OrderedText text = /*Text.literal(String.valueOf(Glyph.ALL[18 + i]));*/Address.asText(String.valueOf(Glyph.ALL[18 + i])).asOrderedText();
            renderer.draw(text, -renderer.getWidth(text) / 2f, -4, colour, false,
                    matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);
            matrices.pop();
        }
        matrices.pop();
        return 0;
    }
}