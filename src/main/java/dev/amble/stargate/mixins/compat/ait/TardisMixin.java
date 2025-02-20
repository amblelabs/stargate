package dev.amble.stargate.mixins.compat.ait;

import dev.amble.stargate.api.StargateLinkable;
import dev.amble.stargate.api.StargateRef;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.data.Exclude;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tardis.class)
public class TardisMixin implements StargateLinkable {
	@Unique
	@Exclude
	private StargateRef ref;

	@Override
	public StargateRef getStargate() {
		return ref;
	}

	@Override
	public void setStargate(StargateRef gate) {
		this.ref = gate;
	}
}
