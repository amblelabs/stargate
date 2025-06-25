package dev.amble.stargate.client.models;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.client.animations.StargateAnimations;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class StargateModel extends BaseStargateModel {
	public final ModelPart stargate;
	public final ModelPart portal;
	public final ModelPart iris;
	public final ModelPart blade;
	public final ModelPart blade2;
	public final ModelPart blade3;
	public final ModelPart blade4;
	public final ModelPart blade5;
	public final ModelPart blade6;
	public final ModelPart blade7;
	public final ModelPart blade8;
	public final ModelPart blade9;
	public final ModelPart blade10;
	public final ModelPart blade11;
	public final ModelPart blade12;
	public final ModelPart blade13;
	public final ModelPart blade14;
	public final ModelPart blade15;
	public final ModelPart blade16;
	public final ModelPart blade17;
	public final ModelPart blade18;
	public final ModelPart OuterRing;
	public final ModelPart chev;
	public final ModelPart chev2;
	public final ModelPart chev3;
	public final ModelPart chev4;
	public final ModelPart chev5;
	public final ModelPart chev6;
	public final ModelPart chev7;
	public final ModelPart chev7bottom;
	public final ModelPart chev8;
	public final ModelPart chev9;
	public final ModelPart lights;
	public final ModelPart chev_light;
	public final ModelPart chev_light2;
	public final ModelPart chev_light3;
	public final ModelPart chev_light4;
	public final ModelPart chev_light5;
	public final ModelPart chev_light6;
	public final ModelPart chev_light7;
	public final ModelPart chev_light7bottom;
	public final ModelPart chev_light8;
	public final ModelPart chev_light9;
	public final ModelPart SymbolRing;
	public final ModelPart InnerRing;
	public StargateModel(ModelPart root) {
		this.stargate = root.getChild("stargate");
		this.portal = this.stargate.getChild("portal");
		this.iris = this.stargate.getChild("iris");
		this.blade = this.iris.getChild("blade");
		this.blade2 = this.iris.getChild("blade2");
		this.blade3 = this.iris.getChild("blade3");
		this.blade4 = this.iris.getChild("blade4");
		this.blade5 = this.iris.getChild("blade5");
		this.blade6 = this.iris.getChild("blade6");
		this.blade7 = this.iris.getChild("blade7");
		this.blade8 = this.iris.getChild("blade8");
		this.blade9 = this.iris.getChild("blade9");
		this.blade10 = this.iris.getChild("blade10");
		this.blade11 = this.iris.getChild("blade11");
		this.blade12 = this.iris.getChild("blade12");
		this.blade13 = this.iris.getChild("blade13");
		this.blade14 = this.iris.getChild("blade14");
		this.blade15 = this.iris.getChild("blade15");
		this.blade16 = this.iris.getChild("blade16");
		this.blade17 = this.iris.getChild("blade17");
		this.blade18 = this.iris.getChild("blade18");
		this.OuterRing = this.stargate.getChild("OuterRing");
		this.chev = this.OuterRing.getChild("chev");
		this.chev2 = this.OuterRing.getChild("chev2");
		this.chev3 = this.OuterRing.getChild("chev3");
		this.chev4 = this.OuterRing.getChild("chev4");
		this.chev5 = this.OuterRing.getChild("chev5");
		this.chev6 = this.OuterRing.getChild("chev6");
		this.chev7 = this.OuterRing.getChild("chev7");
		this.chev7bottom = this.OuterRing.getChild("chev7bottom");
		this.chev8 = this.OuterRing.getChild("chev8");
		this.chev9 = this.OuterRing.getChild("chev9");
		this.lights = this.OuterRing.getChild("lights");
		this.chev_light = this.lights.getChild("chev_light");
		this.chev_light2 = this.lights.getChild("chev_light2");
		this.chev_light3 = this.lights.getChild("chev_light3");
		this.chev_light4 = this.lights.getChild("chev_light4");
		this.chev_light5 = this.lights.getChild("chev_light5");
		this.chev_light6 = this.lights.getChild("chev_light6");
		this.chev_light7 = this.lights.getChild("chev_light7");
		this.chev_light7bottom = this.lights.getChild("chev_light7bottom");
		this.chev_light8 = this.lights.getChild("chev_light8");
		this.chev_light9 = this.lights.getChild("chev_light9");
		this.SymbolRing = this.stargate.getChild("SymbolRing");
		this.InnerRing = this.stargate.getChild("InnerRing");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData stargate = modelPartData.addChild("stargate", ModelPartBuilder.create(), ModelTransform.of(0.0F, -32.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData portal = stargate.addChild("portal", ModelPartBuilder.create().uv(0, 0).cuboid(-41.0F, -97.0F, -1.0F, 82.0F, 82.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 56.0F, 0.0F));

		ModelPartData iris = stargate.addChild("iris", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -2.0F));

		ModelPartData blade = iris.addChild("blade", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 40.0F, 1.0F));

		ModelPartData cube_r1 = blade.addChild("cube_r1", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(0.0F, -40.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

		ModelPartData blade2 = iris.addChild("blade2", ModelPartBuilder.create(), ModelTransform.pivot(-13.0F, 38.0F, 1.0F));

		ModelPartData cube_r2 = blade2.addChild("cube_r2", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(13.0F, -38.0F, 0.0F, 0.0F, 0.0436F, 0.3491F));

		ModelPartData blade3 = iris.addChild("blade3", ModelPartBuilder.create(), ModelTransform.pivot(-25.0F, 31.0F, 1.0F));

		ModelPartData cube_r3 = blade3.addChild("cube_r3", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(25.0F, -31.0F, 0.0F, 0.0F, 0.0436F, 0.6981F));

		ModelPartData blade4 = iris.addChild("blade4", ModelPartBuilder.create(), ModelTransform.pivot(-35.0F, 21.0F, 1.0F));

		ModelPartData cube_r4 = blade4.addChild("cube_r4", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(35.0F, -21.0F, 0.0F, 0.0F, 0.0436F, 1.0472F));

		ModelPartData blade5 = iris.addChild("blade5", ModelPartBuilder.create(), ModelTransform.pivot(-39.0F, 8.0F, 1.0F));

		ModelPartData cube_r5 = blade5.addChild("cube_r5", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(39.0F, -8.0F, 0.0F, 0.0F, 0.0436F, 1.3963F));

		ModelPartData blade6 = iris.addChild("blade6", ModelPartBuilder.create(), ModelTransform.pivot(-40.0F, -7.0F, 1.0F));

		ModelPartData cube_r6 = blade6.addChild("cube_r6", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(40.0F, 7.0F, 0.0F, 0.0F, 0.0436F, 1.7453F));

		ModelPartData blade7 = iris.addChild("blade7", ModelPartBuilder.create(), ModelTransform.pivot(-35.0F, -19.0F, 1.0F));

		ModelPartData cube_r7 = blade7.addChild("cube_r7", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(35.0F, 19.0F, 0.0F, 0.0F, 0.0436F, 2.0944F));

		ModelPartData blade8 = iris.addChild("blade8", ModelPartBuilder.create(), ModelTransform.pivot(-27.0F, -30.0F, 1.0F));

		ModelPartData cube_r8 = blade8.addChild("cube_r8", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(27.0F, 30.0F, 0.0F, 0.0F, 0.0436F, 2.4435F));

		ModelPartData blade9 = iris.addChild("blade9", ModelPartBuilder.create(), ModelTransform.pivot(-15.0F, -38.0F, 1.0F));

		ModelPartData cube_r9 = blade9.addChild("cube_r9", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(15.0F, 38.0F, 0.0F, 0.0F, 0.0436F, 2.7925F));

		ModelPartData blade10 = iris.addChild("blade10", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -40.0F, 1.0F));

		ModelPartData cube_r10 = blade10.addChild("cube_r10", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(0.0F, 40.0F, 0.0F, 0.0F, 0.0436F, -3.1416F));

		ModelPartData blade11 = iris.addChild("blade11", ModelPartBuilder.create(), ModelTransform.pivot(13.0F, -38.0F, 1.0F));

		ModelPartData cube_r11 = blade11.addChild("cube_r11", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-13.0F, 38.0F, 0.0F, 0.0F, 0.0436F, -2.7925F));

		ModelPartData blade12 = iris.addChild("blade12", ModelPartBuilder.create(), ModelTransform.pivot(25.0F, -31.0F, 1.0F));

		ModelPartData cube_r12 = blade12.addChild("cube_r12", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-25.0F, 31.0F, 0.0F, 0.0F, 0.0436F, -2.4435F));

		ModelPartData blade13 = iris.addChild("blade13", ModelPartBuilder.create(), ModelTransform.pivot(34.0F, -20.0F, 1.0F));

		ModelPartData cube_r13 = blade13.addChild("cube_r13", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-34.0F, 20.0F, 0.0F, 0.0F, 0.0436F, -2.0944F));

		ModelPartData blade14 = iris.addChild("blade14", ModelPartBuilder.create(), ModelTransform.pivot(39.0F, -8.0F, 1.0F));

		ModelPartData cube_r14 = blade14.addChild("cube_r14", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-39.0F, 8.0F, 0.0F, 0.0F, 0.0436F, -1.7453F));

		ModelPartData blade15 = iris.addChild("blade15", ModelPartBuilder.create(), ModelTransform.pivot(40.0F, 6.0F, 1.0F));

		ModelPartData cube_r15 = blade15.addChild("cube_r15", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-40.0F, -6.0F, 0.0F, 0.0F, 0.0436F, -1.3963F));

		ModelPartData blade16 = iris.addChild("blade16", ModelPartBuilder.create(), ModelTransform.pivot(35.0F, 19.0F, 1.0F));

		ModelPartData cube_r16 = blade16.addChild("cube_r16", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-35.0F, -19.0F, 0.0F, 0.0F, 0.0436F, -1.0472F));

		ModelPartData blade17 = iris.addChild("blade17", ModelPartBuilder.create(), ModelTransform.pivot(27.0F, 30.0F, 1.0F));

		ModelPartData cube_r17 = blade17.addChild("cube_r17", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-27.0F, -30.0F, 0.0F, 0.0F, 0.0436F, -0.6981F));

		ModelPartData blade18 = iris.addChild("blade18", ModelPartBuilder.create(), ModelTransform.pivot(15.0F, 37.0F, 1.0F));

		ModelPartData cube_r18 = blade18.addChild("cube_r18", ModelPartBuilder.create().uv(1, 88).cuboid(-7.0F, 0.0F, -1.0F, 16.0F, 40.0F, 0.0F, new Dilation(0.01F)), ModelTransform.of(-15.0F, -37.0F, 0.0F, 0.0F, 0.0436F, -0.3491F));

		ModelPartData OuterRing = stargate.addChild("OuterRing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r19 = OuterRing.addChild("cube_r19", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F))
				.uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData cube_r20 = OuterRing.addChild("cube_r20", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r21 = OuterRing.addChild("cube_r21", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

		ModelPartData cube_r22 = OuterRing.addChild("cube_r22", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r23 = OuterRing.addChild("cube_r23", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		ModelPartData cube_r24 = OuterRing.addChild("cube_r24", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r25 = OuterRing.addChild("cube_r25", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		ModelPartData cube_r26 = OuterRing.addChild("cube_r26", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r27 = OuterRing.addChild("cube_r27", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r28 = OuterRing.addChild("cube_r28", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r29 = OuterRing.addChild("cube_r29", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r30 = OuterRing.addChild("cube_r30", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r31 = OuterRing.addChild("cube_r31", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		ModelPartData cube_r32 = OuterRing.addChild("cube_r32", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r33 = OuterRing.addChild("cube_r33", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		ModelPartData cube_r34 = OuterRing.addChild("cube_r34", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r35 = OuterRing.addChild("cube_r35", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.25F))
				.uv(0, 0).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

		ModelPartData cube_r36 = OuterRing.addChild("cube_r36", ModelPartBuilder.create().uv(0, 52).cuboid(-10.0F, 50.0F, -4.5F, 20.0F, 6.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData chev = OuterRing.addChild("chev", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData chev2 = OuterRing.addChild("chev2", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData chev3 = OuterRing.addChild("chev3", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData chev4 = OuterRing.addChild("chev4", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData chev5 = OuterRing.addChild("chev5", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData chev6 = OuterRing.addChild("chev6", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData chev7 = OuterRing.addChild("chev7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r37 = chev7.addChild("cube_r37", ModelPartBuilder.create().uv(70, 51).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData chev7bottom = OuterRing.addChild("chev7bottom", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r38 = chev7bottom.addChild("cube_r38", ModelPartBuilder.create().uv(0, 68).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData chev8 = OuterRing.addChild("chev8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r39 = chev8.addChild("cube_r39", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData chev9 = OuterRing.addChild("chev9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r40 = chev9.addChild("cube_r40", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData lights = OuterRing.addChild("lights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData chev_light = lights.addChild("chev_light", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData chev_light2 = lights.addChild("chev_light2", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData chev_light3 = lights.addChild("chev_light3", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData chev_light4 = lights.addChild("chev_light4", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData chev_light5 = lights.addChild("chev_light5", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData chev_light6 = lights.addChild("chev_light6", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData chev_light7 = lights.addChild("chev_light7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r41 = chev_light7.addChild("cube_r41", ModelPartBuilder.create().uv(24, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData chev_light7bottom = lights.addChild("chev_light7bottom", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r42 = chev_light7bottom.addChild("cube_r42", ModelPartBuilder.create().uv(70, 89).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData chev_light8 = lights.addChild("chev_light8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r43 = chev_light8.addChild("cube_r43", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData chev_light9 = lights.addChild("chev_light9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r44 = chev_light9.addChild("cube_r44", ModelPartBuilder.create().uv(70, 109).cuboid(-10.0F, 46.0F, -4.5F, 20.0F, 10.0F, 9.0F, new Dilation(0.255F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData SymbolRing = stargate.addChild("SymbolRing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r45 = SymbolRing.addChild("cube_r45", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.618F));

		ModelPartData cube_r46 = SymbolRing.addChild("cube_r46", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.9671F));

		ModelPartData cube_r47 = SymbolRing.addChild("cube_r47", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r48 = SymbolRing.addChild("cube_r48", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

		ModelPartData cube_r49 = SymbolRing.addChild("cube_r49", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.9199F));

		ModelPartData cube_r50 = SymbolRing.addChild("cube_r50", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.2689F));

		ModelPartData cube_r51 = SymbolRing.addChild("cube_r51", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r52 = SymbolRing.addChild("cube_r52", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		ModelPartData cube_r53 = SymbolRing.addChild("cube_r53", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

		ModelPartData cube_r54 = SymbolRing.addChild("cube_r54", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		ModelPartData cube_r55 = SymbolRing.addChild("cube_r55", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r56 = SymbolRing.addChild("cube_r56", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		ModelPartData cube_r57 = SymbolRing.addChild("cube_r57", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r58 = SymbolRing.addChild("cube_r58", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		ModelPartData cube_r59 = SymbolRing.addChild("cube_r59", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r60 = SymbolRing.addChild("cube_r60", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r61 = SymbolRing.addChild("cube_r61", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		ModelPartData cube_r62 = SymbolRing.addChild("cube_r62", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		ModelPartData cube_r63 = SymbolRing.addChild("cube_r63", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r64 = SymbolRing.addChild("cube_r64", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r65 = SymbolRing.addChild("cube_r65", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

		ModelPartData cube_r66 = SymbolRing.addChild("cube_r66", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData cube_r67 = SymbolRing.addChild("cube_r67", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r68 = SymbolRing.addChild("cube_r68", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		ModelPartData cube_r69 = SymbolRing.addChild("cube_r69", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData cube_r70 = SymbolRing.addChild("cube_r70", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

		ModelPartData cube_r71 = SymbolRing.addChild("cube_r71", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r72 = SymbolRing.addChild("cube_r72", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		ModelPartData cube_r73 = SymbolRing.addChild("cube_r73", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.2689F));

		ModelPartData cube_r74 = SymbolRing.addChild("cube_r74", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.9199F));

		ModelPartData cube_r75 = SymbolRing.addChild("cube_r75", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r76 = SymbolRing.addChild("cube_r76", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

		ModelPartData cube_r77 = SymbolRing.addChild("cube_r77", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.618F));

		ModelPartData cube_r78 = SymbolRing.addChild("cube_r78", ModelPartBuilder.create().uv(59, 18).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.9671F));

		ModelPartData cube_r79 = SymbolRing.addChild("cube_r79", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r80 = SymbolRing.addChild("cube_r80", ModelPartBuilder.create().uv(59, 0).cuboid(-9.5F, 42.0F, -3.5F, 19.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData InnerRing = stargate.addChild("InnerRing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r81 = InnerRing.addChild("cube_r81", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.7925F));

		ModelPartData cube_r82 = InnerRing.addChild("cube_r82", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.4435F));

		ModelPartData cube_r83 = InnerRing.addChild("cube_r83", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0944F));

		ModelPartData cube_r84 = InnerRing.addChild("cube_r84", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.7453F));

		ModelPartData cube_r85 = InnerRing.addChild("cube_r85", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));

		ModelPartData cube_r86 = InnerRing.addChild("cube_r86", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		ModelPartData cube_r87 = InnerRing.addChild("cube_r87", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData cube_r88 = InnerRing.addChild("cube_r88", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r89 = InnerRing.addChild("cube_r89", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r90 = InnerRing.addChild("cube_r90", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r91 = InnerRing.addChild("cube_r91", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData cube_r92 = InnerRing.addChild("cube_r92", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		ModelPartData cube_r93 = InnerRing.addChild("cube_r93", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));

		ModelPartData cube_r94 = InnerRing.addChild("cube_r94", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.7453F));

		ModelPartData cube_r95 = InnerRing.addChild("cube_r95", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData cube_r96 = InnerRing.addChild("cube_r96", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.4435F));

		ModelPartData cube_r97 = InnerRing.addChild("cube_r97", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7925F));

		ModelPartData cube_r98 = InnerRing.addChild("cube_r98", ModelPartBuilder.create().uv(59, 36).cuboid(-8.0F, 40.0F, -4.0F, 16.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.iris.visible = false; // Hide the iris for this model
		stargate.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return stargate;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		//this.getPart().traverse().forEach(ModelPart::resetTransform);

	}

	@Override
	public Animation getAnimationForState(Stargate.GateState state) {
		return switch(state) {
			//case DIALING, PREOPEN, OPEN -> StargateAnimations.LOCK_SYMBOL;
			default -> Animation.Builder.create(0).build();
		};
	}
}