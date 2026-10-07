package dev.amblelabs.stargate.common.impl.ecs.state;

import dev.amblelabs.stargate.api.StargateAPI;
import dev.drtheo.ecs.state.TState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record LevelState(ServerLevel level, BlockPos pos) implements TState<LevelState> {

    public static final Type<LevelState> state = new Type<>(StargateAPI.modLoc("level"));

    @Override
    public Type<LevelState> type() {
        return state;
    }
}
