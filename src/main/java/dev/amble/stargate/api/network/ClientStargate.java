package dev.amble.stargate.api.network;

import net.minecraft.nbt.NbtCompound;

public class ClientStargate extends Stargate {
	private boolean aged = false;

	public ClientStargate(NbtCompound nbt) {
		super(nbt);
	}

	@Override
	public void age() {
		this.aged = true;
	}

	@Override
	public boolean isAged() {
		return aged;
	}
}
