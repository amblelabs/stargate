package dev.amble.stargate.api.v3.state.stargate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class OrlinState extends GateIdentityState {

    public static final Box NS_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);
    public static final Box WE_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);

    public OrlinState() {
        super(NS_BOX, WE_BOX);
    }
}
