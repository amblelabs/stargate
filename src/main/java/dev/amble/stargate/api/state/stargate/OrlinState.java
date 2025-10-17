package dev.amble.stargate.api.state.stargate;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.GateShape;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class OrlinState extends GateIdentityState {

    public static final Identifier ID = StargateMod.id("orlin");

    public static final Box NS_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);
    public static final Box WE_BOX = new Box(BlockPos.ORIGIN).expand(0, 0, 0).expand(0, 1, 0);

    public OrlinState() {
        this.northSouthBox = NS_BOX;
        this.westEastBox = WE_BOX;

        this.shape = GateShape.EMPTY;
    }
}
