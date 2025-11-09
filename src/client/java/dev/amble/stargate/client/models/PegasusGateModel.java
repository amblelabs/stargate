package dev.amble.stargate.client.models;

import net.minecraft.client.model.ModelPart;

public class PegasusGateModel extends StargateModel {

	public final ModelPart stargate;
	public final ModelPart chev_light;
	public final ModelPart chev_light2;
	public final ModelPart chev_light3;
	public final ModelPart chev_light4;
	public final ModelPart chev_light5;
	public final ModelPart chev_light6;
	public final ModelPart chev_light7;
	public final ModelPart chev_light8;
	public final ModelPart chev_light9;
	public final ModelPart chev_light7bottom;
	public final ModelPart SymbolRing;
	public final ModelPart iris;

	public PegasusGateModel(ModelPart root) {
		super(root);

		this.stargate = root.getChild("stargate");

		ModelPart OuterRing = this.stargate.getChild("OuterRing");
		ModelPart lights = OuterRing.getChild("lights");

		this.chev_light = lights.getChild("chev_light");
		this.chev_light2 = lights.getChild("chev_light2");
		this.chev_light3 = lights.getChild("chev_light3");
		this.chev_light4 = lights.getChild("chev_light4");
		this.chev_light5 = lights.getChild("chev_light5");
		this.chev_light6 = lights.getChild("chev_light6");
		this.chev_light7 = lights.getChild("chev_light7");
		this.chev_light7bottom = lights.getChild("chev_light7bottom");
		this.chev_light8 = lights.getChild("chev_light8");
		this.chev_light9 = lights.getChild("chev_light9");
		this.SymbolRing = this.stargate.getChild("SymbolRing");
		this.iris = this.stargate.getChild("iris");
	}

	@Override
	public ModelPart getPart() {
		return stargate;
	}

	@Override
	public ModelPart[] bakeChevronLights() {
		return new ModelPart[] {
				chev_light, chev_light2, chev_light3, chev_light4,
				chev_light5, chev_light6, chev_light7, chev_light7bottom
		};
	}
}