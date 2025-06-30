package dev.amble.stargate.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DHDModel extends SinglePartEntityModel {
	public final ModelPart dhd;
	public final ModelPart right;
	public final ModelPart rightback;
	public final ModelPart left;
	public final ModelPart leftback;
	public final ModelPart body;
	public final ModelPart main;
	public final ModelPart area;
	public final ModelPart dialbutton;
	public final ModelPart dialbuttonlight;
	public final ModelPart buttons;
	public final ModelPart bottom;
	public final ModelPart bottomlights;
	public final ModelPart button1;
	public final ModelPart button2;
	public final ModelPart button3;
	public final ModelPart button4;
	public final ModelPart button5;
	public final ModelPart button6;
	public final ModelPart button7;
	public final ModelPart button8;
	public final ModelPart button9;
	public final ModelPart button10;
	public final ModelPart button11;
	public final ModelPart button12;
	public final ModelPart button13;
	public final ModelPart button14;
	public final ModelPart button15;
	public final ModelPart button16;
	public final ModelPart button17;
	public final ModelPart button18;
	public final ModelPart top;
	public final ModelPart toplights;
	public final ModelPart button19;
	public final ModelPart button20;
	public final ModelPart button21;
	public final ModelPart button22;
	public final ModelPart button23;
	public final ModelPart button24;
	public final ModelPart button25;
	public final ModelPart button26;
	public final ModelPart button27;
	public final ModelPart button28;
	public final ModelPart button29;
	public final ModelPart button30;
	public final ModelPart button31;
	public final ModelPart button32;
	public final ModelPart button33;
	public final ModelPart button34;
	public final ModelPart button35;
	public final ModelPart button36;
	public DHDModel(ModelPart root) {
		this.dhd = root.getChild("dhd");
		this.right = this.dhd.getChild("right");
		this.rightback = this.dhd.getChild("rightback");
		this.left = this.dhd.getChild("left");
		this.leftback = this.dhd.getChild("leftback");
		this.body = this.dhd.getChild("body");
		this.main = this.dhd.getChild("main");
		this.area = this.main.getChild("area");
		this.dialbutton = this.main.getChild("dialbutton");
		this.dialbuttonlight = this.dialbutton.getChild("dialbuttonlight");
		this.buttons = this.main.getChild("buttons");
		this.bottom = this.buttons.getChild("bottom");
		this.bottomlights = this.bottom.getChild("bottomlights");
		this.button1 = this.bottomlights.getChild("button1");
		this.button2 = this.bottomlights.getChild("button2");
		this.button3 = this.bottomlights.getChild("button3");
		this.button4 = this.bottomlights.getChild("button4");
		this.button5 = this.bottomlights.getChild("button5");
		this.button6 = this.bottomlights.getChild("button6");
		this.button7 = this.bottomlights.getChild("button7");
		this.button8 = this.bottomlights.getChild("button8");
		this.button9 = this.bottomlights.getChild("button9");
		this.button10 = this.bottomlights.getChild("button10");
		this.button11 = this.bottomlights.getChild("button11");
		this.button12 = this.bottomlights.getChild("button12");
		this.button13 = this.bottomlights.getChild("button13");
		this.button14 = this.bottomlights.getChild("button14");
		this.button15 = this.bottomlights.getChild("button15");
		this.button16 = this.bottomlights.getChild("button16");
		this.button17 = this.bottomlights.getChild("button17");
		this.button18 = this.bottomlights.getChild("button18");
		this.top = this.buttons.getChild("top");
		this.toplights = this.top.getChild("toplights");
		this.button19 = this.toplights.getChild("button19");
		this.button20 = this.toplights.getChild("button20");
		this.button21 = this.toplights.getChild("button21");
		this.button22 = this.toplights.getChild("button22");
		this.button23 = this.toplights.getChild("button23");
		this.button24 = this.toplights.getChild("button24");
		this.button25 = this.toplights.getChild("button25");
		this.button26 = this.toplights.getChild("button26");
		this.button27 = this.toplights.getChild("button27");
		this.button28 = this.toplights.getChild("button28");
		this.button29 = this.toplights.getChild("button29");
		this.button30 = this.toplights.getChild("button30");
		this.button31 = this.toplights.getChild("button31");
		this.button32 = this.toplights.getChild("button32");
		this.button33 = this.toplights.getChild("button33");
		this.button34 = this.toplights.getChild("button34");
		this.button35 = this.toplights.getChild("button35");
		this.button36 = this.toplights.getChild("button36");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData dhd = modelPartData.addChild("dhd", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData right = dhd.addChild("right", ModelPartBuilder.create(), ModelTransform.of(4.5F, -1.0F, -4.5F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r1 = right.addChild("cube_r1", ModelPartBuilder.create().uv(0, 98).cuboid(0.0F, -11.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.5629F, -9.5433F, 0.5629F, -0.6155F, -0.5236F, 0.9553F));

		ModelPartData cube_r2 = right.addChild("cube_r2", ModelPartBuilder.create().uv(13, 104).cuboid(0.0F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.0607F, -4.0F, -1.0607F, -0.3655F, -0.7119F, 0.5299F));

		ModelPartData cube_r3 = right.addChild("cube_r3", ModelPartBuilder.create().uv(26, 104).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData rightback = dhd.addChild("rightback", ModelPartBuilder.create(), ModelTransform.of(5.5F, -1.0F, 3.5F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r4 = rightback.addChild("cube_r4", ModelPartBuilder.create().uv(94, -2).cuboid(-1.5307F, -14.6955F, -1.5F, 3.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0607F, -4.0F, -1.0607F, -0.3655F, -0.7119F, 0.5299F));

		ModelPartData cube_r5 = rightback.addChild("cube_r5", ModelPartBuilder.create().uv(97, 52).cuboid(-1.5F, -8.0F, -1.5F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData left = dhd.addChild("left", ModelPartBuilder.create(), ModelTransform.of(-4.5F, -1.0F, -4.5F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r6 = left.addChild("cube_r6", ModelPartBuilder.create().uv(95, 15).cuboid(-3.0F, -11.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.5629F, -9.5433F, 0.5629F, -0.6155F, 0.5236F, -0.9553F));

		ModelPartData cube_r7 = left.addChild("cube_r7", ModelPartBuilder.create().uv(98, 66).cuboid(-3.0F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.0607F, -4.0F, -1.0607F, -0.3655F, 0.7119F, -0.5299F));

		ModelPartData cube_r8 = left.addChild("cube_r8", ModelPartBuilder.create().uv(39, 104).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData leftback = dhd.addChild("leftback", ModelPartBuilder.create(), ModelTransform.of(-5.5F, -1.0F, 3.5F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r9 = leftback.addChild("cube_r9", ModelPartBuilder.create().uv(91, 93).cuboid(-1.4693F, -14.6955F, -1.5F, 3.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0607F, -4.0F, -1.0607F, -0.3655F, 0.7119F, -0.5299F));

		ModelPartData cube_r10 = leftback.addChild("cube_r10", ModelPartBuilder.create().uv(94, 29).cuboid(-1.5F, -8.0F, -1.5F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData body = dhd.addChild("body", ModelPartBuilder.create().uv(0, 23).cuboid(-5.0F, -6.0F, -9.0F, 10.0F, 15.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -9.0F, 5.0F));

		ModelPartData cube_r11 = body.addChild("cube_r11", ModelPartBuilder.create().uv(39, 23).cuboid(-6.0F, -9.0F, -3.0F, 12.0F, 9.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		ModelPartData main = dhd.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, -10.5F, -5.0F, 0.4363F, 0.0F, 0.0F));

		ModelPartData area = main.addChild("area", ModelPartBuilder.create().uv(2, 2).cuboid(-11.0F, -13.0845F, -10.1495F, 26.0F, 1.0F, 19.0F, new Dilation(0.002F))
				.uv(39, 36).cuboid(-1.5F, -15.1345F, -4.0995F, 7.0F, 0.0F, 7.0F, new Dilation(0.001F)), ModelTransform.pivot(-2.0F, 11.6345F, 8.1495F));

		ModelPartData cube_r12 = area.addChild("cube_r12", ModelPartBuilder.create().uv(98, 80).cuboid(-10.1768F, -2.0F, 11.8232F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.8063F, -11.6355F, -5.4244F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r13 = area.addChild("cube_r13", ModelPartBuilder.create().uv(108, 8).cuboid(-9.1154F, -2.0F, -0.2218F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, -3.2627F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r14 = area.addChild("cube_r14", ModelPartBuilder.create().uv(108, 16).cuboid(-9.0F, -2.0F, -0.75F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(14.4315F, -11.6355F, 6.6341F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r15 = area.addChild("cube_r15", ModelPartBuilder.create().uv(108, 4).cuboid(-9.1154F, -2.0F, -0.7782F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, 2.5309F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r16 = area.addChild("cube_r16", ModelPartBuilder.create().uv(78, 104).cuboid(-9.1768F, -2.0F, -0.8232F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(15.7044F, -11.6345F, -2.3784F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r17 = area.addChild("cube_r17", ModelPartBuilder.create().uv(98, 76).cuboid(2.1768F, -2.0F, 11.8232F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-1.8051F, -11.6365F, -5.4238F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r18 = area.addChild("cube_r18", ModelPartBuilder.create().uv(108, 0).cuboid(5.1154F, -2.0F, -0.2218F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, -3.262F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r19 = area.addChild("cube_r19", ModelPartBuilder.create().uv(108, 12).cuboid(5.0F, -2.0F, -0.75F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-10.4302F, -11.6365F, 6.6348F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r20 = area.addChild("cube_r20", ModelPartBuilder.create().uv(105, 106).cuboid(5.1154F, -2.0F, -0.7782F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, 2.5316F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r21 = area.addChild("cube_r21", ModelPartBuilder.create().uv(65, 104).cuboid(4.1768F, -2.0F, -0.8232F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-11.7031F, -11.6355F, -2.3777F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r22 = area.addChild("cube_r22", ModelPartBuilder.create().uv(105, 102).cuboid(-7.617F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.6356F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r23 = area.addChild("cube_r23", ModelPartBuilder.create().uv(53, 104).cuboid(-4.117F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r24 = area.addChild("cube_r24", ModelPartBuilder.create().uv(105, 98).cuboid(-3.6285F, -1.0F, -0.7891F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0984F, -12.6345F, -9.7887F, 0.0F, 0.3054F, 0.0F));

		ModelPartData cube_r25 = area.addChild("cube_r25", ModelPartBuilder.create().uv(105, 94).cuboid(-0.3715F, -1.0F, -0.7891F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, -0.3054F, 0.0F));

		ModelPartData dialbutton = main.addChild("dialbutton", ModelPartBuilder.create().uv(24, 47).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
				.uv(8, 47).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -2.9F, 7.3F));

		ModelPartData dialbuttonlight = dialbutton.addChild("dialbuttonlight", ModelPartBuilder.create().uv(24, 53).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.05F))
				.uv(24, 53).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.55F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData buttons = main.addChild("buttons", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.5F, 7.5F));

		ModelPartData bottom = buttons.addChild("bottom", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r26 = bottom.addChild("cube_r26", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData cube_r27 = bottom.addChild("cube_r27", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r28 = bottom.addChild("cube_r28", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData cube_r29 = bottom.addChild("cube_r29", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 1.3963F, 3.1416F));

		ModelPartData cube_r30 = bottom.addChild("cube_r30", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r31 = bottom.addChild("cube_r31", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.6981F, 3.1416F));

		ModelPartData cube_r32 = bottom.addChild("cube_r32", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.3491F, 3.1416F));

		ModelPartData cube_r33 = bottom.addChild("cube_r33", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r34 = bottom.addChild("cube_r34", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r35 = bottom.addChild("cube_r35", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData cube_r36 = bottom.addChild("cube_r36", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData cube_r37 = bottom.addChild("cube_r37", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r38 = bottom.addChild("cube_r38", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData cube_r39 = bottom.addChild("cube_r39", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData cube_r40 = bottom.addChild("cube_r40", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r41 = bottom.addChild("cube_r41", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData cube_r42 = bottom.addChild("cube_r42", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData bottomlights = bottom.addChild("bottomlights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.05F, 0.0F));

		ModelPartData button1 = bottomlights.addChild("button1", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button2 = bottomlights.addChild("button2", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button3 = bottomlights.addChild("button3", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button4 = bottomlights.addChild("button4", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button5 = bottomlights.addChild("button5", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button6 = bottomlights.addChild("button6", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button7 = bottomlights.addChild("button7", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button8 = bottomlights.addChild("button8", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button9 = bottomlights.addChild("button9", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button10 = bottomlights.addChild("button10", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button11 = bottomlights.addChild("button11", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button12 = bottomlights.addChild("button12", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button13 = bottomlights.addChild("button13", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button14 = bottomlights.addChild("button14", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button15 = bottomlights.addChild("button15", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button16 = bottomlights.addChild("button16", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button17 = bottomlights.addChild("button17", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button18 = bottomlights.addChild("button18", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData top = buttons.addChild("top", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData cube_r43 = top.addChild("cube_r43", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 1.3963F, 3.1416F));

		ModelPartData cube_r44 = top.addChild("cube_r44", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r45 = top.addChild("cube_r45", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData cube_r46 = top.addChild("cube_r46", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.6981F, 3.1416F));

		ModelPartData cube_r47 = top.addChild("cube_r47", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.3491F, 3.1416F));

		ModelPartData cube_r48 = top.addChild("cube_r48", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r49 = top.addChild("cube_r49", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData cube_r50 = top.addChild("cube_r50", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r51 = top.addChild("cube_r51", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r52 = top.addChild("cube_r52", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.6981F, 3.1416F));

		ModelPartData cube_r53 = top.addChild("cube_r53", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r54 = top.addChild("cube_r54", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.3491F, 3.1416F));

		ModelPartData cube_r55 = top.addChild("cube_r55", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData cube_r56 = top.addChild("cube_r56", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r57 = top.addChild("cube_r57", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -1.3963F, 3.1416F));

		ModelPartData cube_r58 = top.addChild("cube_r58", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData cube_r59 = top.addChild("cube_r59", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData toplights = top.addChild("toplights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.05F, 0.0F));

		ModelPartData button19 = toplights.addChild("button19", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button20 = toplights.addChild("button20", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button21 = toplights.addChild("button21", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button22 = toplights.addChild("button22", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button23 = toplights.addChild("button23", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button24 = toplights.addChild("button24", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button25 = toplights.addChild("button25", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button26 = toplights.addChild("button26", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button27 = toplights.addChild("button27", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button28 = toplights.addChild("button28", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button29 = toplights.addChild("button29", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button30 = toplights.addChild("button30", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button31 = toplights.addChild("button31", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button32 = toplights.addChild("button32", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button33 = toplights.addChild("button33", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button34 = toplights.addChild("button34", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button35 = toplights.addChild("button35", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button36 = toplights.addChild("button36", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		dhd.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return dhd;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}