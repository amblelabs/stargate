package dev.amble.stargate.client.models;

import dev.amble.lib.client.model.BlockEntityModel;
import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class DHDModel extends BlockEntityModel {
	public final ModelPart dhd;
	public final ModelPart main;
	public final ModelPart area;
	public final ModelPart dialbutton;
	public final ModelPart dialbuttonlight;
	public final ModelPart rings;
	public final ModelPart bottom;
	public final ModelPart lights;
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
	public final ModelPart bits;
	public final ModelPart button37;
	public final ModelPart button38;
	public final ModelPart button39;
	public final ModelPart button40;
	public final ModelPart button41;
	public final ModelPart button42;
	public final ModelPart button43;
	public final ModelPart button44;
	public final ModelPart button45;
	public final ModelPart button46;
	public final ModelPart button47;
	public final ModelPart button48;
	public final ModelPart button49;
	public final ModelPart button50;
	public final ModelPart button51;
	public final ModelPart button52;
	public final ModelPart button53;
	public final ModelPart button54;
	public final ModelPart top;
	public final ModelPart lights2;
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
	public final ModelPart bits2;
	public final ModelPart button55;
	public final ModelPart button56;
	public final ModelPart button57;
	public final ModelPart button58;
	public final ModelPart button59;
	public final ModelPart button60;
	public final ModelPart button61;
	public final ModelPart button62;
	public final ModelPart button63;
	public final ModelPart button64;
	public final ModelPart button65;
	public final ModelPart button66;
	public final ModelPart button67;
	public final ModelPart button68;
	public final ModelPart button69;
	public final ModelPart button70;
	public final ModelPart button71;
	public final ModelPart button72;
	public final ModelPart bone;
	public final ModelPart bone4;
	public final ModelPart bone2;
	public final ModelPart bone3;

	public DHDModel() {
		this(getTexturedModelData().createModel());
	}

	private DHDModel(ModelPart root) {
		this.dhd = root.getChild("dhd");
		this.main = this.dhd.getChild("main");
		this.area = this.main.getChild("area");
		this.dialbutton = this.main.getChild("dialbutton");
		this.dialbuttonlight = this.main.getChild("dialbuttonlight");
		this.rings = this.main.getChild("rings");
		this.bottom = this.rings.getChild("bottom");
		this.lights = this.bottom.getChild("lights");
		this.button1 = this.lights.getChild("button1");
		this.button2 = this.lights.getChild("button2");
		this.button3 = this.lights.getChild("button3");
		this.button4 = this.lights.getChild("button4");
		this.button5 = this.lights.getChild("button5");
		this.button6 = this.lights.getChild("button6");
		this.button7 = this.lights.getChild("button7");
		this.button8 = this.lights.getChild("button8");
		this.button9 = this.lights.getChild("button9");
		this.button10 = this.lights.getChild("button10");
		this.button11 = this.lights.getChild("button11");
		this.button12 = this.lights.getChild("button12");
		this.button13 = this.lights.getChild("button13");
		this.button14 = this.lights.getChild("button14");
		this.button15 = this.lights.getChild("button15");
		this.button16 = this.lights.getChild("button16");
		this.button17 = this.lights.getChild("button17");
		this.button18 = this.lights.getChild("button18");
		this.bits = this.bottom.getChild("bits");
		this.button37 = this.bits.getChild("button37");
		this.button38 = this.bits.getChild("button38");
		this.button39 = this.bits.getChild("button39");
		this.button40 = this.bits.getChild("button40");
		this.button41 = this.bits.getChild("button41");
		this.button42 = this.bits.getChild("button42");
		this.button43 = this.bits.getChild("button43");
		this.button44 = this.bits.getChild("button44");
		this.button45 = this.bits.getChild("button45");
		this.button46 = this.bits.getChild("button46");
		this.button47 = this.bits.getChild("button47");
		this.button48 = this.bits.getChild("button48");
		this.button49 = this.bits.getChild("button49");
		this.button50 = this.bits.getChild("button50");
		this.button51 = this.bits.getChild("button51");
		this.button52 = this.bits.getChild("button52");
		this.button53 = this.bits.getChild("button53");
		this.button54 = this.bits.getChild("button54");
		this.top = this.rings.getChild("top");
		this.lights2 = this.top.getChild("lights2");
		this.button19 = this.lights2.getChild("button19");
		this.button20 = this.lights2.getChild("button20");
		this.button21 = this.lights2.getChild("button21");
		this.button22 = this.lights2.getChild("button22");
		this.button23 = this.lights2.getChild("button23");
		this.button24 = this.lights2.getChild("button24");
		this.button25 = this.lights2.getChild("button25");
		this.button26 = this.lights2.getChild("button26");
		this.button27 = this.lights2.getChild("button27");
		this.button28 = this.lights2.getChild("button28");
		this.button29 = this.lights2.getChild("button29");
		this.button30 = this.lights2.getChild("button30");
		this.button31 = this.lights2.getChild("button31");
		this.button32 = this.lights2.getChild("button32");
		this.button33 = this.lights2.getChild("button33");
		this.button34 = this.lights2.getChild("button34");
		this.button35 = this.lights2.getChild("button35");
		this.button36 = this.lights2.getChild("button36");
		this.bits2 = this.top.getChild("bits2");
		this.button55 = this.bits2.getChild("button55");
		this.button56 = this.bits2.getChild("button56");
		this.button57 = this.bits2.getChild("button57");
		this.button58 = this.bits2.getChild("button58");
		this.button59 = this.bits2.getChild("button59");
		this.button60 = this.bits2.getChild("button60");
		this.button61 = this.bits2.getChild("button61");
		this.button62 = this.bits2.getChild("button62");
		this.button63 = this.bits2.getChild("button63");
		this.button64 = this.bits2.getChild("button64");
		this.button65 = this.bits2.getChild("button65");
		this.button66 = this.bits2.getChild("button66");
		this.button67 = this.bits2.getChild("button67");
		this.button68 = this.bits2.getChild("button68");
		this.button69 = this.bits2.getChild("button69");
		this.button70 = this.bits2.getChild("button70");
		this.button71 = this.bits2.getChild("button71");
		this.button72 = this.bits2.getChild("button72");
		this.bone = this.dhd.getChild("bone");
		this.bone4 = this.dhd.getChild("bone4");
		this.bone2 = this.dhd.getChild("bone2");
		this.bone3 = this.dhd.getChild("bone3");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData dhd = modelPartData.addChild("dhd", ModelPartBuilder.create().uv(0, 23).cuboid(-5.0F, -15.0F, -4.0F, 10.0F, 15.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = dhd.addChild("cube_r1", ModelPartBuilder.create().uv(39, 23).cuboid(-6.0F, -9.0F, -3.0F, 12.0F, 9.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, 5.0F, -0.3927F, 0.0F, 0.0F));

		ModelPartData main = dhd.addChild("main", ModelPartBuilder.create(), ModelTransform.of(0.0F, -10.5F, -5.0F, 0.4363F, 0.0F, 0.0F));

		ModelPartData area = main.addChild("area", ModelPartBuilder.create().uv(2, 2).cuboid(-11.0F, -13.0845F, -10.1495F, 26.0F, 1.0F, 19.0F, new Dilation(0.002F))
				.uv(39, 36).cuboid(-1.5F, -15.1345F, -4.0995F, 7.0F, 0.0F, 7.0F, new Dilation(0.001F)), ModelTransform.pivot(-2.0F, 11.6345F, 8.1495F));

		ModelPartData cube_r2 = area.addChild("cube_r2", ModelPartBuilder.create().uv(98, 80).cuboid(-10.1768F, -2.0F, 11.8232F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.8063F, -11.6355F, -5.4244F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r3 = area.addChild("cube_r3", ModelPartBuilder.create().uv(108, 8).cuboid(-9.1154F, -2.0F, -0.2218F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, -3.2627F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r4 = area.addChild("cube_r4", ModelPartBuilder.create().uv(108, 16).cuboid(-9.0F, -2.0F, -0.75F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(14.4315F, -11.6355F, 6.6341F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r5 = area.addChild("cube_r5", ModelPartBuilder.create().uv(108, 4).cuboid(-9.1154F, -2.0F, -0.7782F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.8532F, -11.6355F, 2.5309F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r6 = area.addChild("cube_r6", ModelPartBuilder.create().uv(78, 104).cuboid(-9.1768F, -2.0F, -0.8232F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(15.7044F, -11.6345F, -2.3784F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r7 = area.addChild("cube_r7", ModelPartBuilder.create().uv(98, 76).cuboid(2.1768F, -2.0F, 11.8232F, 8.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-1.8051F, -11.6365F, -5.4238F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r8 = area.addChild("cube_r8", ModelPartBuilder.create().uv(108, 0).cuboid(5.1154F, -2.0F, -0.2218F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, -3.262F, 0.0F, -1.0908F, 0.0F));

		ModelPartData cube_r9 = area.addChild("cube_r9", ModelPartBuilder.create().uv(108, 12).cuboid(5.0F, -2.0F, -0.75F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-10.4302F, -11.6365F, 6.6348F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r10 = area.addChild("cube_r10", ModelPartBuilder.create().uv(105, 106).cuboid(5.1154F, -2.0F, -0.7782F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.852F, -11.6365F, 2.5316F, 0.0F, 1.0908F, 0.0F));

		ModelPartData cube_r11 = area.addChild("cube_r11", ModelPartBuilder.create().uv(65, 104).cuboid(4.1768F, -2.0F, -0.8232F, 5.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-11.7031F, -11.6355F, -2.3777F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r12 = area.addChild("cube_r12", ModelPartBuilder.create().uv(105, 102).cuboid(-7.617F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(5.6356F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r13 = area.addChild("cube_r13", ModelPartBuilder.create().uv(53, 104).cuboid(-4.117F, -1.0F, -0.8643F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r14 = area.addChild("cube_r14", ModelPartBuilder.create().uv(105, 98).cuboid(-3.6285F, -1.0F, -0.7891F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0984F, -12.6345F, -9.7887F, 0.0F, 0.3054F, 0.0F));

		ModelPartData cube_r15 = area.addChild("cube_r15", ModelPartBuilder.create().uv(105, 94).cuboid(-0.3715F, -1.0F, -0.7891F, 4.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(6.0984F, -12.6345F, -9.7887F, 0.0F, -0.3054F, 0.0F));

		ModelPartData dialbutton = main.addChild("dialbutton", ModelPartBuilder.create().uv(24, 47).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
				.uv(8, 47).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -2.9F, 7.3F));

		ModelPartData dialbuttonlight = main.addChild("dialbuttonlight", ModelPartBuilder.create().uv(24, 53).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
				.uv(8, 53).cuboid(-2.0F, -1.5F, -1.75F, 4.0F, 2.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -2.9F, 7.3F));

		ModelPartData rings = main.addChild("rings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.5F, 7.5F));

		ModelPartData bottom = rings.addChild("bottom", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r16 = bottom.addChild("cube_r16", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData cube_r17 = bottom.addChild("cube_r17", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r18 = bottom.addChild("cube_r18", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData cube_r19 = bottom.addChild("cube_r19", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 1.3963F, 3.1416F));

		ModelPartData cube_r20 = bottom.addChild("cube_r20", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r21 = bottom.addChild("cube_r21", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.6981F, 3.1416F));

		ModelPartData cube_r22 = bottom.addChild("cube_r22", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.3491F, 3.1416F));

		ModelPartData cube_r23 = bottom.addChild("cube_r23", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r24 = bottom.addChild("cube_r24", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r25 = bottom.addChild("cube_r25", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData cube_r26 = bottom.addChild("cube_r26", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData cube_r27 = bottom.addChild("cube_r27", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r28 = bottom.addChild("cube_r28", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData cube_r29 = bottom.addChild("cube_r29", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData cube_r30 = bottom.addChild("cube_r30", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r31 = bottom.addChild("cube_r31", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData cube_r32 = bottom.addChild("cube_r32", ModelPartBuilder.create().uv(73, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData lights = bottom.addChild("lights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.05F, 0.0F));

		ModelPartData button1 = lights.addChild("button1", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button2 = lights.addChild("button2", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button3 = lights.addChild("button3", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button4 = lights.addChild("button4", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button5 = lights.addChild("button5", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button6 = lights.addChild("button6", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button7 = lights.addChild("button7", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button8 = lights.addChild("button8", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.3214F, 0.0F, 0.383F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button9 = lights.addChild("button9", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button10 = lights.addChild("button10", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button11 = lights.addChild("button11", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button12 = lights.addChild("button12", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button13 = lights.addChild("button13", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button14 = lights.addChild("button14", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button15 = lights.addChild("button15", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button16 = lights.addChild("button16", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button17 = lights.addChild("button17", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button18 = lights.addChild("button18", ModelPartBuilder.create().uv(67, 6).cuboid(-1.5F, -1.0F, -0.5F, 3.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData bits = bottom.addChild("bits", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.1F, 0.0F));

		ModelPartData button37 = bits.addChild("button37", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button38 = bits.addChild("button38", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button39 = bits.addChild("button39", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button40 = bits.addChild("button40", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button41 = bits.addChild("button41", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button42 = bits.addChild("button42", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button43 = bits.addChild("button43", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button44 = bits.addChild("button44", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.75F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.3214F, 0.0F, 0.383F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button45 = bits.addChild("button45", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button46 = bits.addChild("button46", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button47 = bits.addChild("button47", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button48 = bits.addChild("button48", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button49 = bits.addChild("button49", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button50 = bits.addChild("button50", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button51 = bits.addChild("button51", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button52 = bits.addChild("button52", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button53 = bits.addChild("button53", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button54 = bits.addChild("button54", ModelPartBuilder.create().uv(83, 6).cuboid(-1.0F, -1.0F, 2.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData top = rings.addChild("top", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData cube_r33 = top.addChild("cube_r33", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 1.3963F, 3.1416F));

		ModelPartData cube_r34 = top.addChild("cube_r34", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r35 = top.addChild("cube_r35", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData cube_r36 = top.addChild("cube_r36", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.6981F, 3.1416F));

		ModelPartData cube_r37 = top.addChild("cube_r37", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.3491F, 3.1416F));

		ModelPartData cube_r38 = top.addChild("cube_r38", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r39 = top.addChild("cube_r39", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData cube_r40 = top.addChild("cube_r40", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r41 = top.addChild("cube_r41", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r42 = top.addChild("cube_r42", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.6981F, 3.1416F));

		ModelPartData cube_r43 = top.addChild("cube_r43", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r44 = top.addChild("cube_r44", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.3491F, 3.1416F));

		ModelPartData cube_r45 = top.addChild("cube_r45", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData cube_r46 = top.addChild("cube_r46", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r47 = top.addChild("cube_r47", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -1.3963F, 3.1416F));

		ModelPartData cube_r48 = top.addChild("cube_r48", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData cube_r49 = top.addChild("cube_r49", ModelPartBuilder.create().uv(2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData lights2 = top.addChild("lights2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.05F, 0.0F));

		ModelPartData button19 = lights2.addChild("button19", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button20 = lights2.addChild("button20", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button21 = lights2.addChild("button21", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button22 = lights2.addChild("button22", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button23 = lights2.addChild("button23", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button24 = lights2.addChild("button24", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button25 = lights2.addChild("button25", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button26 = lights2.addChild("button26", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button27 = lights2.addChild("button27", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button28 = lights2.addChild("button28", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button29 = lights2.addChild("button29", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button30 = lights2.addChild("button30", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button31 = lights2.addChild("button31", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button32 = lights2.addChild("button32", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button33 = lights2.addChild("button33", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button34 = lights2.addChild("button34", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button35 = lights2.addChild("button35", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button36 = lights2.addChild("button36", ModelPartBuilder.create().uv(-2, 14).cuboid(-1.0F, -1.0F, -0.25F, 2.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData bits2 = top.addChild("bits2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.3F, 0.0F));

		ModelPartData button55 = bits2.addChild("button55", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button56 = bits2.addChild("button56", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		ModelPartData button57 = bits2.addChild("button57", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		ModelPartData button58 = bits2.addChild("button58", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData button59 = bits2.addChild("button59", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 0.0F));

		ModelPartData button60 = bits2.addChild("button60", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 0.0F));

		ModelPartData button61 = bits2.addChild("button61", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData button62 = bits2.addChild("button62", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.4435F, 0.0F));

		ModelPartData button63 = bits2.addChild("button63", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.7925F, 0.0F));

		ModelPartData button64 = bits2.addChild("button64", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData button65 = bits2.addChild("button65", ModelPartBuilder.create().uv(14, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.7925F, 0.0F));

		ModelPartData button66 = bits2.addChild("button66", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.4435F, 0.0F));

		ModelPartData button67 = bits2.addChild("button67", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData button68 = bits2.addChild("button68", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.7453F, 0.0F));

		ModelPartData button69 = bits2.addChild("button69", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		ModelPartData button70 = bits2.addChild("button70", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData button71 = bits2.addChild("button71", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		ModelPartData button72 = bits2.addChild("button72", ModelPartBuilder.create().uv(15, 14).cuboid(-0.5F, -0.8F, 3.5F, 1.0F, 0.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		ModelPartData bone = dhd.addChild("bone", ModelPartBuilder.create(), ModelTransform.of(4.5F, -1.0F, -4.5F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r50 = bone.addChild("cube_r50", ModelPartBuilder.create().uv(0, 98).cuboid(0.0F, -11.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.5629F, -9.5433F, 0.5629F, -0.6155F, -0.5236F, 0.9553F));

		ModelPartData cube_r51 = bone.addChild("cube_r51", ModelPartBuilder.create().uv(13, 104).cuboid(0.0F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.0607F, -4.0F, -1.0607F, -0.3655F, -0.7119F, 0.5299F));

		ModelPartData cube_r52 = bone.addChild("cube_r52", ModelPartBuilder.create().uv(26, 104).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData bone4 = dhd.addChild("bone4", ModelPartBuilder.create(), ModelTransform.of(5.5F, -1.0F, 3.5F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r53 = bone4.addChild("cube_r53", ModelPartBuilder.create().uv(94, -2).cuboid(-1.5307F, -14.6955F, -1.5F, 3.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0607F, -4.0F, -1.0607F, -0.3655F, -0.7119F, 0.5299F));

		ModelPartData cube_r54 = bone4.addChild("cube_r54", ModelPartBuilder.create().uv(97, 52).cuboid(-1.5F, -8.0F, -1.5F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData bone2 = dhd.addChild("bone2", ModelPartBuilder.create(), ModelTransform.of(-4.5F, -1.0F, -4.5F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r55 = bone2.addChild("cube_r55", ModelPartBuilder.create().uv(95, 15).cuboid(-3.0F, -11.0F, -1.5F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.5629F, -9.5433F, 0.5629F, -0.6155F, 0.5236F, -0.9553F));

		ModelPartData cube_r56 = bone2.addChild("cube_r56", ModelPartBuilder.create().uv(98, 66).cuboid(-3.0F, -6.0F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.0607F, -4.0F, -1.0607F, -0.3655F, 0.7119F, -0.5299F));

		ModelPartData cube_r57 = bone2.addChild("cube_r57", ModelPartBuilder.create().uv(39, 104).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData bone3 = dhd.addChild("bone3", ModelPartBuilder.create(), ModelTransform.of(-5.5F, -1.0F, 3.5F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r58 = bone3.addChild("cube_r58", ModelPartBuilder.create().uv(91, 93).cuboid(-1.4693F, -14.6955F, -1.5F, 3.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0607F, -4.0F, -1.0607F, -0.3655F, 0.7119F, -0.5299F));

		ModelPartData cube_r59 = bone3.addChild("cube_r59", ModelPartBuilder.create().uv(94, 29).cuboid(-1.5F, -8.0F, -1.5F, 3.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public ModelPart getPart() {
		return dhd;
	}
}