package dev.amble.stargate.api.gates.state.stargate;

import dev.amble.stargate.StargateMod;
import dev.amble.stargate.api.GateShape;
import dev.drtheo.yaar.state.TState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class GateIdentityState implements TState<GateIdentityState> {

    public static final Box NS_DEFAULT = new Box(BlockPos.ORIGIN).expand(2, 2, 0).expand(0, 3, 0);
    public static final Box WE_DEFAULT = new Box(BlockPos.ORIGIN).expand(0, 2, 2).expand(0, 3, 0);

    public static final Type<GateIdentityState> state = new Type<>(StargateMod.id("gate"));

    public Box northSouthBox = NS_DEFAULT;
    public Box westEastBox = WE_DEFAULT;

    public int maxChevrons = 9;
    public GateShape shape = GateShape.DEFAULT;

    public Box forDirection(Direction direction) {
        return direction == Direction.NORTH || direction == Direction.SOUTH ? northSouthBox : westEastBox;
    }

    @Override
    public Type<GateIdentityState> type() {
        return state;
    }
}
