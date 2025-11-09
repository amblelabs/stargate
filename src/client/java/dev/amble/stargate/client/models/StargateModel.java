package dev.amble.stargate.client.models;

import net.minecraft.client.model.*;

import java.util.function.Function;

public abstract class StargateModel extends BaseStargateModel {

    protected StargateModel(ModelPart root) { }

    public static <T extends StargateModel> T create(Function<ModelPart, T> f) {
        return f.apply(getTexturedModelData().createModel());
    }

    private static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData stargate = modelPartData.addChild("stargate", ModelPartBuilder.create(), ModelTransform.of(0.0F, -32.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData OuterRing = stargate.addChild("OuterRing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev = OuterRing.addChild("chev", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData chev2 = OuterRing.addChild("chev2", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData chev3 = OuterRing.addChild("chev3", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData chev4 = OuterRing.addChild("chev4", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData chev5 = OuterRing.addChild("chev5", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData chev6 = OuterRing.addChild("chev6", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData chev7 = OuterRing.addChild("chev7", ModelPartBuilder.create().uv(70, 51).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev7mid = OuterRing.addChild("chev7mid", ModelPartBuilder.create().uv(129, 51).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev7bottom = OuterRing.addChild("chev7bottom", ModelPartBuilder.create().uv(0, 68).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev8 = OuterRing.addChild("chev8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = chev8.addChild("cube_r1", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData chev9 = OuterRing.addChild("chev9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = chev9.addChild("cube_r2", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData lights = OuterRing.addChild("lights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev_light = lights.addChild("chev_light", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData chev_light2 = lights.addChild("chev_light2", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData chev_light3 = lights.addChild("chev_light3", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData chev_light4 = lights.addChild("chev_light4", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData chev_light5 = lights.addChild("chev_light5", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData chev_light6 = lights.addChild("chev_light6", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData chev_light7 = lights.addChild("chev_light7", ModelPartBuilder.create().uv(0, 138).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev_light7bottom = lights.addChild("chev_light7bottom", ModelPartBuilder.create().uv(70, 89).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData chev_light8 = lights.addChild("chev_light8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r3 = chev_light8.addChild("cube_r3", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData chev_light9 = lights.addChild("chev_light9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r4 = chev_light9.addChild("cube_r4", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData main = OuterRing.addChild("main", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r5 = main.addChild("cube_r5", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData cube_r6 = main.addChild("cube_r6", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r7 = main.addChild("cube_r7", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r8 = main.addChild("cube_r8", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r9 = main.addChild("cube_r9", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r10 = main.addChild("cube_r10", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r11 = main.addChild("cube_r11", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r12 = main.addChild("cube_r12", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r13 = main.addChild("cube_r13", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r14 = main.addChild("cube_r14", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r15 = main.addChild("cube_r15", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r16 = main.addChild("cube_r16", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r17 = main.addChild("cube_r17", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r18 = main.addChild("cube_r18", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r19 = main.addChild("cube_r19", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r20 = main.addChild("cube_r20", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
                .uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r21 = main.addChild("cube_r21", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData destiny = OuterRing.addChild("destiny", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r22 = destiny.addChild("cube_r22", ModelPartBuilder.create().uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F))
                .uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData cube_r23 = destiny.addChild("cube_r23", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r24 = destiny.addChild("cube_r24", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r25 = destiny.addChild("cube_r25", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r26 = destiny.addChild("cube_r26", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r27 = destiny.addChild("cube_r27", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r28 = destiny.addChild("cube_r28", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r29 = destiny.addChild("cube_r29", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r30 = destiny.addChild("cube_r30", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r31 = destiny.addChild("cube_r31", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r32 = destiny.addChild("cube_r32", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r33 = destiny.addChild("cube_r33", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r34 = destiny.addChild("cube_r34", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r35 = destiny.addChild("cube_r35", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r36 = destiny.addChild("cube_r36", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r37 = destiny.addChild("cube_r37", ModelPartBuilder.create().uv(124, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.25F))
                .uv(185, 110).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r38 = destiny.addChild("cube_r38", ModelPartBuilder.create().uv(183, 142).cuboid(-10.0F, 51.0F, -4.5F, 20.0F, 5.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData accessorylights = OuterRing.addChild("accessorylights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r39 = accessorylights.addChild("cube_r39", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r40 = accessorylights.addChild("cube_r40", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r41 = accessorylights.addChild("cube_r41", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r42 = accessorylights.addChild("cube_r42", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData cube_r43 = accessorylights.addChild("cube_r43", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r44 = accessorylights.addChild("cube_r44", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r45 = accessorylights.addChild("cube_r45", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r46 = accessorylights.addChild("cube_r46", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r47 = accessorylights.addChild("cube_r47", ModelPartBuilder.create().uv(65, 141).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData SymbolRing = stargate.addChild("SymbolRing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData Extras = SymbolRing.addChild("Extras", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData cube_r48 = Extras.addChild("cube_r48", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.618F));

        ModelPartData cube_r49 = Extras.addChild("cube_r49", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.9671F));

        ModelPartData cube_r50 = Extras.addChild("cube_r50", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        ModelPartData cube_r51 = Extras.addChild("cube_r51", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.2689F));

        ModelPartData cube_r52 = Extras.addChild("cube_r52", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        ModelPartData cube_r53 = Extras.addChild("cube_r53", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData cube_r54 = Extras.addChild("cube_r54", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        ModelPartData cube_r55 = Extras.addChild("cube_r55", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

        ModelPartData cube_r56 = Extras.addChild("cube_r56", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartData cube_r57 = Extras.addChild("cube_r57", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData cube_r58 = Extras.addChild("cube_r58", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

        ModelPartData cube_r59 = Extras.addChild("cube_r59", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        ModelPartData cube_r60 = Extras.addChild("cube_r60", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        ModelPartData cube_r61 = Extras.addChild("cube_r61", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        ModelPartData cube_r62 = Extras.addChild("cube_r62", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.2689F));

        ModelPartData cube_r63 = Extras.addChild("cube_r63", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

        ModelPartData cube_r64 = Extras.addChild("cube_r64", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.618F));

        ModelPartData cube_r65 = Extras.addChild("cube_r65", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9671F));

        ModelPartData Extras2 = SymbolRing.addChild("Extras2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r66 = Extras2.addChild("cube_r66", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.618F));

        ModelPartData cube_r67 = Extras2.addChild("cube_r67", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.9671F));

        ModelPartData cube_r68 = Extras2.addChild("cube_r68", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

        ModelPartData cube_r69 = Extras2.addChild("cube_r69", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.2689F));

        ModelPartData cube_r70 = Extras2.addChild("cube_r70", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        ModelPartData cube_r71 = Extras2.addChild("cube_r71", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData cube_r72 = Extras2.addChild("cube_r72", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        ModelPartData cube_r73 = Extras2.addChild("cube_r73", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

        ModelPartData cube_r74 = Extras2.addChild("cube_r74", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartData cube_r75 = Extras2.addChild("cube_r75", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData cube_r76 = Extras2.addChild("cube_r76", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

        ModelPartData cube_r77 = Extras2.addChild("cube_r77", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        ModelPartData cube_r78 = Extras2.addChild("cube_r78", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        ModelPartData cube_r79 = Extras2.addChild("cube_r79", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        ModelPartData cube_r80 = Extras2.addChild("cube_r80", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.2689F));

        ModelPartData cube_r81 = Extras2.addChild("cube_r81", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

        ModelPartData cube_r82 = Extras2.addChild("cube_r82", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.618F));

        ModelPartData cube_r83 = Extras2.addChild("cube_r83", ModelPartBuilder.create().uv(179, 42).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9671F));

        ModelPartData Base = SymbolRing.addChild("Base", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r84 = Base.addChild("cube_r84", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r85 = Base.addChild("cube_r85", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r86 = Base.addChild("cube_r86", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r87 = Base.addChild("cube_r87", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r88 = Base.addChild("cube_r88", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r89 = Base.addChild("cube_r89", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r90 = Base.addChild("cube_r90", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r91 = Base.addChild("cube_r91", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r92 = Base.addChild("cube_r92", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r93 = Base.addChild("cube_r93", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r94 = Base.addChild("cube_r94", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r95 = Base.addChild("cube_r95", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r96 = Base.addChild("cube_r96", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r97 = Base.addChild("cube_r97", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r98 = Base.addChild("cube_r98", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r99 = Base.addChild("cube_r99", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r100 = Base.addChild("cube_r100", ModelPartBuilder.create().uv(112, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData Base2 = SymbolRing.addChild("Base2", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData cube_r101 = Base2.addChild("cube_r101", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r102 = Base2.addChild("cube_r102", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r103 = Base2.addChild("cube_r103", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r104 = Base2.addChild("cube_r104", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r105 = Base2.addChild("cube_r105", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r106 = Base2.addChild("cube_r106", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r107 = Base2.addChild("cube_r107", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r108 = Base2.addChild("cube_r108", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r109 = Base2.addChild("cube_r109", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r110 = Base2.addChild("cube_r110", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r111 = Base2.addChild("cube_r111", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r112 = Base2.addChild("cube_r112", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r113 = Base2.addChild("cube_r113", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r114 = Base2.addChild("cube_r114", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r115 = Base2.addChild("cube_r115", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r116 = Base2.addChild("cube_r116", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r117 = Base2.addChild("cube_r117", ModelPartBuilder.create().uv(132, 71).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData BaseLights = SymbolRing.addChild("BaseLights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData BaseLight1 = BaseLights.addChild("BaseLight1", ModelPartBuilder.create().uv(112, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData BaseLight2 = BaseLights.addChild("BaseLight2", ModelPartBuilder.create().uv(165, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.051F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r118 = BaseLight2.addChild("cube_r118", ModelPartBuilder.create().uv(165, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData BaseLight3 = BaseLights.addChild("BaseLight3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r119 = BaseLight3.addChild("cube_r119", ModelPartBuilder.create().uv(112, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData BaseLights2 = SymbolRing.addChild("BaseLights2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData BaseLight4 = BaseLights2.addChild("BaseLight4", ModelPartBuilder.create().uv(112, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData BaseLight5 = BaseLights2.addChild("BaseLight5", ModelPartBuilder.create().uv(165, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.051F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r120 = BaseLight5.addChild("cube_r120", ModelPartBuilder.create().uv(165, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData BaseLight6 = BaseLights2.addChild("BaseLight6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r121 = BaseLight6.addChild("cube_r121", ModelPartBuilder.create().uv(112, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.05F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData SymbolRingDestiny = stargate.addChild("SymbolRingDestiny", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData DestExtras = SymbolRingDestiny.addChild("DestExtras", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0698F));

        ModelPartData cube_r122 = DestExtras.addChild("cube_r122", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r123 = DestExtras.addChild("cube_r123", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9548F));

        ModelPartData cube_r124 = DestExtras.addChild("cube_r124", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5359F));

        ModelPartData cube_r125 = DestExtras.addChild("cube_r125", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6755F));

        ModelPartData cube_r126 = DestExtras.addChild("cube_r126", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.8151F));

        ModelPartData cube_r127 = DestExtras.addChild("cube_r127", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0718F));

        ModelPartData cube_r128 = DestExtras.addChild("cube_r128", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5133F));

        ModelPartData cube_r129 = DestExtras.addChild("cube_r129", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.6529F));

        ModelPartData cube_r130 = DestExtras.addChild("cube_r130", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r131 = DestExtras.addChild("cube_r131", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.9322F));

        ModelPartData cube_r132 = DestExtras.addChild("cube_r132", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r133 = DestExtras.addChild("cube_r133", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.234F));

        ModelPartData cube_r134 = DestExtras.addChild("cube_r134", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3736F));

        ModelPartData cube_r135 = DestExtras.addChild("cube_r135", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

        ModelPartData cube_r136 = DestExtras.addChild("cube_r136", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2793F));

        ModelPartData cube_r137 = DestExtras.addChild("cube_r137", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1396F));

        ModelPartData cube_r138 = DestExtras.addChild("cube_r138", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4189F));

        ModelPartData cube_r139 = DestExtras.addChild("cube_r139", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5585F));

        ModelPartData cube_r140 = DestExtras.addChild("cube_r140", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r141 = DestExtras.addChild("cube_r141", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8378F));

        ModelPartData cube_r142 = DestExtras.addChild("cube_r142", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r143 = DestExtras.addChild("cube_r143", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5585F));

        ModelPartData cube_r144 = DestExtras.addChild("cube_r144", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4189F));

        ModelPartData cube_r145 = DestExtras.addChild("cube_r145", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2793F));

        ModelPartData cube_r146 = DestExtras.addChild("cube_r146", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.117F));

        ModelPartData cube_r147 = DestExtras.addChild("cube_r147", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9774F));

        ModelPartData cube_r148 = DestExtras.addChild("cube_r148", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8378F));

        ModelPartData cube_r149 = DestExtras.addChild("cube_r149", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2566F));

        ModelPartData cube_r150 = DestExtras.addChild("cube_r150", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5359F));

        ModelPartData cube_r151 = DestExtras.addChild("cube_r151", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.6755F));

        ModelPartData cube_r152 = DestExtras.addChild("cube_r152", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.8151F));

        ModelPartData cube_r153 = DestExtras.addChild("cube_r153", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.9548F));

        ModelPartData cube_r154 = DestExtras.addChild("cube_r154", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.117F));

        ModelPartData cube_r155 = DestExtras.addChild("cube_r155", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2566F));

        ModelPartData cube_r156 = DestExtras.addChild("cube_r156", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r157 = DestExtras.addChild("cube_r157", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9774F));

        ModelPartData cube_r158 = DestExtras.addChild("cube_r158", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.234F));

        ModelPartData cube_r159 = DestExtras.addChild("cube_r159", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3736F));

        ModelPartData cube_r160 = DestExtras.addChild("cube_r160", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.5133F));

        ModelPartData cube_r161 = DestExtras.addChild("cube_r161", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r162 = DestExtras.addChild("cube_r162", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.6529F));

        ModelPartData cube_r163 = DestExtras.addChild("cube_r163", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r164 = DestExtras.addChild("cube_r164", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9322F));

        ModelPartData cube_r165 = DestExtras.addChild("cube_r165", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.0718F));

        ModelPartData DestBase = SymbolRingDestiny.addChild("DestBase", ModelPartBuilder.create().uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F))
                .uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r166 = DestBase.addChild("cube_r166", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r167 = DestBase.addChild("cube_r167", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r168 = DestBase.addChild("cube_r168", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r169 = DestBase.addChild("cube_r169", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r170 = DestBase.addChild("cube_r170", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r171 = DestBase.addChild("cube_r171", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r172 = DestBase.addChild("cube_r172", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r173 = DestBase.addChild("cube_r173", ModelPartBuilder.create().uv(132, 107).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(132, 89).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r174 = DestBase.addChild("cube_r174", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9548F));

        ModelPartData cube_r175 = DestBase.addChild("cube_r175", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5359F));

        ModelPartData cube_r176 = DestBase.addChild("cube_r176", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.6755F));

        ModelPartData cube_r177 = DestBase.addChild("cube_r177", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.8151F));

        ModelPartData cube_r178 = DestBase.addChild("cube_r178", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0718F));

        ModelPartData cube_r179 = DestBase.addChild("cube_r179", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5133F));

        ModelPartData cube_r180 = DestBase.addChild("cube_r180", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.6529F));

        ModelPartData cube_r181 = DestBase.addChild("cube_r181", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.9322F));

        ModelPartData cube_r182 = DestBase.addChild("cube_r182", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.234F));

        ModelPartData cube_r183 = DestBase.addChild("cube_r183", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3736F));

        ModelPartData cube_r184 = DestBase.addChild("cube_r184", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

        ModelPartData cube_r185 = DestBase.addChild("cube_r185", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2793F));

        ModelPartData cube_r186 = DestBase.addChild("cube_r186", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1396F));

        ModelPartData cube_r187 = DestBase.addChild("cube_r187", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4189F));

        ModelPartData cube_r188 = DestBase.addChild("cube_r188", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5585F));

        ModelPartData cube_r189 = DestBase.addChild("cube_r189", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8378F));

        ModelPartData cube_r190 = DestBase.addChild("cube_r190", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5585F));

        ModelPartData cube_r191 = DestBase.addChild("cube_r191", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4189F));

        ModelPartData cube_r192 = DestBase.addChild("cube_r192", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2793F));

        ModelPartData cube_r193 = DestBase.addChild("cube_r193", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.117F));

        ModelPartData cube_r194 = DestBase.addChild("cube_r194", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9774F));

        ModelPartData cube_r195 = DestBase.addChild("cube_r195", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8378F));

        ModelPartData cube_r196 = DestBase.addChild("cube_r196", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2566F));

        ModelPartData cube_r197 = DestBase.addChild("cube_r197", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5359F));

        ModelPartData cube_r198 = DestBase.addChild("cube_r198", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.6755F));

        ModelPartData cube_r199 = DestBase.addChild("cube_r199", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.8151F));

        ModelPartData cube_r200 = DestBase.addChild("cube_r200", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.9548F));

        ModelPartData cube_r201 = DestBase.addChild("cube_r201", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.117F));

        ModelPartData cube_r202 = DestBase.addChild("cube_r202", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2566F));

        ModelPartData cube_r203 = DestBase.addChild("cube_r203", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9774F));

        ModelPartData cube_r204 = DestBase.addChild("cube_r204", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.234F));

        ModelPartData cube_r205 = DestBase.addChild("cube_r205", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3736F));

        ModelPartData cube_r206 = DestBase.addChild("cube_r206", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.5133F));

        ModelPartData cube_r207 = DestBase.addChild("cube_r207", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.6529F));

        ModelPartData cube_r208 = DestBase.addChild("cube_r208", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9322F));

        ModelPartData cube_r209 = DestBase.addChild("cube_r209", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.0718F));

        ModelPartData InnerRing = stargate.addChild("InnerRing", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r210 = InnerRing.addChild("cube_r210", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r211 = InnerRing.addChild("cube_r211", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r212 = InnerRing.addChild("cube_r212", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r213 = InnerRing.addChild("cube_r213", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r214 = InnerRing.addChild("cube_r214", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r215 = InnerRing.addChild("cube_r215", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r216 = InnerRing.addChild("cube_r216", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r217 = InnerRing.addChild("cube_r217", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r218 = InnerRing.addChild("cube_r218", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r219 = InnerRing.addChild("cube_r219", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r220 = InnerRing.addChild("cube_r220", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r221 = InnerRing.addChild("cube_r221", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r222 = InnerRing.addChild("cube_r222", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r223 = InnerRing.addChild("cube_r223", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r224 = InnerRing.addChild("cube_r224", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r225 = InnerRing.addChild("cube_r225", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.002F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r226 = InnerRing.addChild("cube_r226", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData InnerRingLayer = InnerRing.addChild("InnerRingLayer", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r227 = InnerRingLayer.addChild("cube_r227", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

        ModelPartData cube_r228 = InnerRingLayer.addChild("cube_r228", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

        ModelPartData cube_r229 = InnerRingLayer.addChild("cube_r229", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

        ModelPartData cube_r230 = InnerRingLayer.addChild("cube_r230", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

        ModelPartData cube_r231 = InnerRingLayer.addChild("cube_r231", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

        ModelPartData cube_r232 = InnerRingLayer.addChild("cube_r232", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r233 = InnerRingLayer.addChild("cube_r233", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        ModelPartData cube_r234 = InnerRingLayer.addChild("cube_r234", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r235 = InnerRingLayer.addChild("cube_r235", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r236 = InnerRingLayer.addChild("cube_r236", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

        ModelPartData cube_r237 = InnerRingLayer.addChild("cube_r237", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r238 = InnerRingLayer.addChild("cube_r238", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

        ModelPartData cube_r239 = InnerRingLayer.addChild("cube_r239", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

        ModelPartData cube_r240 = InnerRingLayer.addChild("cube_r240", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

        ModelPartData cube_r241 = InnerRingLayer.addChild("cube_r241", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

        ModelPartData cube_r242 = InnerRingLayer.addChild("cube_r242", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

        ModelPartData cube_r243 = InnerRingLayer.addChild("cube_r243", ModelPartBuilder.create().uv(108, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData iris = stargate.addChild("iris", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -2.0F));

        ModelPartData blade1 = iris.addChild("blade1", ModelPartBuilder.create(), ModelTransform.of(-5.0F, 40.0F, 1.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData cube_r244 = blade1.addChild("cube_r244", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade2 = iris.addChild("blade2", ModelPartBuilder.create(), ModelTransform.of(5.0F, -40.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

        ModelPartData cube_r245 = blade2.addChild("cube_r245", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade3 = iris.addChild("blade3", ModelPartBuilder.create(), ModelTransform.of(40.2606F, -2.0219F, 1.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartData cube_r246 = blade3.addChild("cube_r246", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade4 = iris.addChild("blade4", ModelPartBuilder.create(), ModelTransform.of(-40.2606F, 2.0219F, 1.0F, 0.0F, 0.0F, 2.9671F));

        ModelPartData cube_r247 = blade4.addChild("cube_r247", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade5 = iris.addChild("blade5", ModelPartBuilder.create(), ModelTransform.of(8.9823F, 39.2978F, 1.0F, 0.0F, 0.0F, 1.2217F));

        ModelPartData cube_r248 = blade5.addChild("cube_r248", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade6 = iris.addChild("blade6", ModelPartBuilder.create(), ModelTransform.of(-8.9823F, -39.2978F, 1.0F, 0.0F, 0.0F, -1.9199F));

        ModelPartData cube_r249 = blade6.addChild("cube_r249", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade7 = iris.addChild("blade7", ModelPartBuilder.create(), ModelTransform.of(37.141F, -15.6699F, 1.0F, 0.0F, 0.0F, -0.5236F));

        ModelPartData cube_r250 = blade7.addChild("cube_r250", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade8 = iris.addChild("blade8", ModelPartBuilder.create(), ModelTransform.of(-37.141F, 15.6699F, 1.0F, 0.0F, 0.0F, 2.618F));

        ModelPartData cube_r251 = blade8.addChild("cube_r251", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade9 = iris.addChild("blade9", ModelPartBuilder.create(), ModelTransform.of(21.8813F, 33.8557F, 1.0F, 0.0F, 0.0F, 0.8727F));

        ModelPartData cube_r252 = blade9.addChild("cube_r252", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade10 = iris.addChild("blade10", ModelPartBuilder.create(), ModelTransform.of(-21.8813F, -33.8557F, 1.0F, 0.0F, 0.0F, -2.2689F));

        ModelPartData cube_r253 = blade10.addChild("cube_r253", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade11 = iris.addChild("blade11", ModelPartBuilder.create(), ModelTransform.of(29.5417F, -27.4278F, 1.0F, 0.0F, 0.0F, -0.8727F));

        ModelPartData cube_r254 = blade11.addChild("cube_r254", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade12 = iris.addChild("blade12", ModelPartBuilder.create(), ModelTransform.of(-29.5417F, 27.4278F, 1.0F, 0.0F, 0.0F, 2.2689F));

        ModelPartData cube_r255 = blade12.addChild("cube_r255", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade13 = iris.addChild("blade13", ModelPartBuilder.create(), ModelTransform.of(32.141F, 24.3301F, 1.0F, 0.0F, 0.0F, 0.5236F));

        ModelPartData cube_r256 = blade13.addChild("cube_r256", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade14 = iris.addChild("blade14", ModelPartBuilder.create(), ModelTransform.of(-32.141F, -24.3301F, 1.0F, 0.0F, 0.0F, -2.618F));

        ModelPartData cube_r257 = blade14.addChild("cube_r257", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade15 = iris.addChild("blade15", ModelPartBuilder.create(), ModelTransform.of(18.3793F, -35.8776F, 1.0F, 0.0F, 0.0F, -1.2217F));

        ModelPartData cube_r258 = blade15.addChild("cube_r258", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade16 = iris.addChild("blade16", ModelPartBuilder.create(), ModelTransform.of(-18.3793F, 35.8776F, 1.0F, 0.0F, 0.0F, 1.9199F));

        ModelPartData cube_r259 = blade16.addChild("cube_r259", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade17 = iris.addChild("blade17", ModelPartBuilder.create(), ModelTransform.of(38.5241F, 11.87F, 1.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData cube_r260 = blade17.addChild("cube_r260", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));

        ModelPartData blade18 = iris.addChild("blade18", ModelPartBuilder.create(), ModelTransform.of(-38.5241F, -11.87F, 1.0F, 0.0F, 0.0F, -2.9671F));

        ModelPartData cube_r261 = blade18.addChild("cube_r261", ModelPartBuilder.create().uv(0, 88).cuboid(-9.0F, -7.0F, -1.0F, 18.0F, 50.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(5.0F, -40.0F, 0.0F, 0.0F, 0.0175F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }
}
