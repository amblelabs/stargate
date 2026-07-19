package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.drtheo.ecs.state.TState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LevelState implements TState<LevelState> {

    public static final Type<LevelState> state = new Type<>(StargateAPI.modLoc("level"));

    public ServerLevel level;
    public BlockPos pos;

    public LevelState(ServerLevel level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public BlockState getBlockState() {
        return level.getBlockState(pos);
    }

    @Override
    public Type<LevelState> type() {
        return state;
    }
}
