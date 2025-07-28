package dev.amble.stargate.client.models;

import dev.amble.stargate.api.kernels.GateState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class OrlinGateModel extends BaseStargateModel {
	public final ModelPart stargate;
	public final ModelPart base;
	public final ModelPart gate;
	public final ModelPart innerlayer;
	public final ModelPart chevron7;
	public final ModelPart chevron1;
	public final ModelPart chevron2;
	public final ModelPart chevron3;
	public final ModelPart chevron4;
	public final ModelPart chevron5;
	public final ModelPart chevron6;
	public final ModelPart chevron8;
	public final ModelPart chevron9;
	public final ModelPart outerlayer;
	public final ModelPart steps;

	public OrlinGateModel(ModelPart root) {
		this.stargate = root.getChild("stargate");
		this.base = this.stargate.getChild("base");
		this.gate = this.stargate.getChild("gate");
		this.innerlayer = this.gate.getChild("innerlayer");
		this.chevron7 = this.innerlayer.getChild("chevron7");
		this.chevron1 = this.innerlayer.getChild("chevron1");
		this.chevron2 = this.innerlayer.getChild("chevron2");
		this.chevron3 = this.innerlayer.getChild("chevron3");
		this.chevron4 = this.innerlayer.getChild("chevron4");
		this.chevron5 = this.innerlayer.getChild("chevron5");
		this.chevron6 = this.innerlayer.getChild("chevron6");
		this.chevron8 = this.innerlayer.getChild("chevron8");
		this.chevron9 = this.innerlayer.getChild("chevron9");
		this.outerlayer = this.gate.getChild("outerlayer");
		this.steps = this.stargate.getChild("steps");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stargate = modelPartData.addChild("stargate", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData base = stargate.addChild("base", ModelPartBuilder.create().uv(0, 17).cuboid(-24.0F, -1.0F, 6.0F, 48.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-24.0F, -17.0F, 0.0F, 48.0F, 17.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 21).cuboid(-24.0F, -1.0F, -14.0F, 48.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 25).cuboid(-24.0F, -1.0F, -12.0F, 2.0F, 1.0F, 18.0F, new Dilation(0.0F))
		.uv(41, 25).cuboid(22.0F, -1.0F, -12.0F, 2.0F, 1.0F, 18.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData gate = stargate.addChild("gate", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, 0.5F));

		ModelPartData innerlayer = gate.addChild("innerlayer", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = innerlayer.addChild("cube_r1", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r2 = innerlayer.addChild("cube_r2", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		ModelPartData cube_r3 = innerlayer.addChild("cube_r3", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r4 = innerlayer.addChild("cube_r4", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r5 = innerlayer.addChild("cube_r5", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r6 = innerlayer.addChild("cube_r6", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		ModelPartData cube_r7 = innerlayer.addChild("cube_r7", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData cube_r8 = innerlayer.addChild("cube_r8", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r9 = innerlayer.addChild("cube_r9", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

		ModelPartData cube_r10 = innerlayer.addChild("cube_r10", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r11 = innerlayer.addChild("cube_r11", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

		ModelPartData cube_r12 = innerlayer.addChild("cube_r12", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r13 = innerlayer.addChild("cube_r13", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		ModelPartData cube_r14 = innerlayer.addChild("cube_r14", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r15 = innerlayer.addChild("cube_r15", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		ModelPartData cube_r16 = innerlayer.addChild("cube_r16", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r17 = innerlayer.addChild("cube_r17", ModelPartBuilder.create().uv(63, 67).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData chevron7 = innerlayer.addChild("chevron7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r18 = chevron7.addChild("cube_r18", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron1 = innerlayer.addChild("chevron1", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r19 = chevron1.addChild("cube_r19", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron2 = innerlayer.addChild("chevron2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r20 = chevron2.addChild("cube_r20", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron3 = innerlayer.addChild("chevron3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r21 = chevron3.addChild("cube_r21", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron4 = innerlayer.addChild("chevron4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r22 = chevron4.addChild("cube_r22", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron5 = innerlayer.addChild("chevron5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r23 = chevron5.addChild("cube_r23", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron6 = innerlayer.addChild("chevron6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r24 = chevron6.addChild("cube_r24", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron8 = innerlayer.addChild("chevron8", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r25 = chevron8.addChild("cube_r25", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData chevron9 = innerlayer.addChild("chevron9", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r26 = chevron9.addChild("cube_r26", ModelPartBuilder.create().uv(46, 76).cuboid(-3.5F, 16.0F, -3.0F, 7.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData outerlayer = gate.addChild("outerlayer", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r27 = outerlayer.addChild("cube_r27", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		ModelPartData cube_r28 = outerlayer.addChild("cube_r28", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r29 = outerlayer.addChild("cube_r29", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r30 = outerlayer.addChild("cube_r30", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r31 = outerlayer.addChild("cube_r31", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		ModelPartData cube_r32 = outerlayer.addChild("cube_r32", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r33 = outerlayer.addChild("cube_r33", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData cube_r34 = outerlayer.addChild("cube_r34", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r35 = outerlayer.addChild("cube_r35", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

		ModelPartData cube_r36 = outerlayer.addChild("cube_r36", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r37 = outerlayer.addChild("cube_r37", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

		ModelPartData cube_r38 = outerlayer.addChild("cube_r38", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r39 = outerlayer.addChild("cube_r39", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		ModelPartData cube_r40 = outerlayer.addChild("cube_r40", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r41 = outerlayer.addChild("cube_r41", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		ModelPartData cube_r42 = outerlayer.addChild("cube_r42", ModelPartBuilder.create().uv(63, 75).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r43 = outerlayer.addChild("cube_r43", ModelPartBuilder.create().uv(63, 59).cuboid(-3.5F, 16.0F, -2.0F, 7.0F, 4.0F, 3.0F, new Dilation(0.251F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData steps = stargate.addChild("steps", ModelPartBuilder.create().uv(105, 57).cuboid(-8.0F, -6.0F, -10.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
		.uv(34, 101).cuboid(-8.0F, -6.0F, -4.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(105, 93).cuboid(-8.0F, -6.0F, -11.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(105, 73).cuboid(-8.0F, -3.0F, -15.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(106, 50).cuboid(-8.0F, -3.0F, -16.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(43, 45).cuboid(-7.0F, -2.5F, -15.5F, 14.0F, 0.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 45).cuboid(-7.0F, -5.5F, -10.5F, 14.0F, 0.0F, 7.0F, new Dilation(0.0F))
		.uv(105, 65).cuboid(7.0F, -6.0F, -10.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
		.uv(106, 44).cuboid(7.0F, -6.0F, -11.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(105, 79).cuboid(7.0F, -3.0F, -15.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(63, 106).cuboid(7.0F, -3.0F, -16.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(105, 85).cuboid(7.0F, -6.0F, -4.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		stargate.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return stargate;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

	@Override
	public Animation getAnimationForState(GateState state) {
		return Animation.Builder.create(0).build();
	}
}