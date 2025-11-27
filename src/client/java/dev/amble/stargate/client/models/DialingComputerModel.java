package dev.amble.stargate.client.models;

import dev.amble.lib.client.model.BlockEntityModel;
import net.minecraft.client.model.*;

public class DialingComputerModel extends BlockEntityModel {

    private final ModelPart root;

    public DialingComputerModel() {
        this(getTexturedModelData().createModel());
    }

    private DialingComputerModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData monitor = modelPartData.addChild(
            "monitor",
            ModelPartBuilder.create()
                .uv(0, 30)
                .cuboid(
                    -4.0F,
                    0.6F,
                    -4.0F,
                    8.0F,
                    1.0F,
                    8.0F,
                    new Dilation(0.0F)
                )
                .uv(12, 45)
                .cuboid(
                    -1.0F,
                    -0.4F,
                    -1.0F,
                    2.0F,
                    1.0F,
                    2.0F,
                    new Dilation(0.0F)
                ),
            ModelTransform.of(7.9401F, 22.4F, 3.0863F, 0.0F, 0.1309F, 0.0F)
        );

        ModelPartData cube_r1 = monitor.addChild(
            "cube_r1",
            ModelPartBuilder.create()
                .uv(38, 19)
                .cuboid(
                    -5.0F,
                    -9.0F,
                    2.0F,
                    10.0F,
                    7.0F,
                    3.0F,
                    new Dilation(0.0F)
                )
                .uv(0, 0)
                .cuboid(
                    -6.0F,
                    -11.0F,
                    -6.0F,
                    12.0F,
                    11.0F,
                    8.0F,
                    new Dilation(0.0F)
                )
                .uv(32, 30)
                .cuboid(
                    -6.0F,
                    -11.0F,
                    -6.0F,
                    12.0F,
                    11.0F,
                    1.0F,
                    new Dilation(0.15F)
                ),
            ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1309F, 0.3054F, 0.0F)
        );

        ModelPartData keyboard = modelPartData.addChild(
            "keyboard",
            ModelPartBuilder.create()
                .uv(0, 19)
                .cuboid(
                    -7.0F,
                    -1.0F,
                    -12.0F,
                    14.0F,
                    1.0F,
                    5.0F,
                    new Dilation(0.0F)
                )
                .uv(0, 25)
                .cuboid(
                    -7.0F,
                    -1.2F,
                    -12.0F,
                    14.0F,
                    0.0F,
                    5.0F,
                    new Dilation(0.0F)
                ),
            ModelTransform.pivot(4.0F, 24.0F, 4.0F)
        );

        ModelPartData iris_hand_print = modelPartData.addChild(
            "iris_hand_print",
            ModelPartBuilder.create()
                .uv(42, 46)
                .cuboid(
                    -8.0F,
                    -1.0F,
                    -4.0F,
                    5.0F,
                    1.0F,
                    6.0F,
                    new Dilation(0.0F)
                )
                .uv(-5, 59)
                .cuboid(
                    -8.0F,
                    -0.1F,
                    2.0F,
                    13.0F,
                    0.0F,
                    5.0F,
                    new Dilation(0.0F)
                )
                .uv(40, 9)
                .cuboid(
                    -8.0F,
                    -4.0F,
                    -4.0F,
                    0.0F,
                    4.0F,
                    6.0F,
                    new Dilation(0.0F)
                )
                .uv(40, 9)
                .cuboid(
                    -3.0F,
                    -4.0F,
                    -4.0F,
                    0.0F,
                    4.0F,
                    6.0F,
                    new Dilation(0.0F)
                )
                .uv(40, 6)
                .cuboid(
                    -8.0F,
                    -2.0F,
                    -1.0F,
                    5.0F,
                    1.0F,
                    3.0F,
                    new Dilation(0.0F)
                )
                .uv(32, 42)
                .cuboid(
                    -8.0F,
                    -3.0F,
                    0.0F,
                    5.0F,
                    1.0F,
                    2.0F,
                    new Dilation(0.0F)
                )
                .uv(0, 45)
                .cuboid(
                    -8.0F,
                    -4.0F,
                    1.0F,
                    5.0F,
                    1.0F,
                    1.0F,
                    new Dilation(0.0F)
                ),
            ModelTransform.pivot(0.0F, 24.0F, 1.0F)
        );

        ModelPartData cube_r2 = iris_hand_print.addChild(
            "cube_r2",
            ModelPartBuilder.create()
                .uv(28, 58)
                .cuboid(
                    -3.0F,
                    -0.4F,
                    -2.5F,
                    5.0F,
                    0.0F,
                    6.0F,
                    new Dilation(0.0F)
                )
                .uv(0, 39)
                .cuboid(
                    -3.0F,
                    -0.5F,
                    -2.5F,
                    5.0F,
                    0.0F,
                    6.0F,
                    new Dilation(0.0F)
                ),
            ModelTransform.of(-5.0F, -1.6F, -1.5F, 0.6109F, 0.0F, 0.0F)
        );
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}
