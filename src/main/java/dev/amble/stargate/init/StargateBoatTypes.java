package dev.amble.stargate.init;

import dev.amble.modkit.api.ABoatType;
import dev.amble.modkit.api.BoatTypeContainer;

public class StargateBoatTypes extends BoatTypeContainer {

    public static final ABoatType NAQUADAH = register(StargateItems.NAQUADAH_BOAT, StargateItems.NAQUADAH_CHEST_BOAT, StargateBlocks.NAQUADAH_BLOCK);
}
