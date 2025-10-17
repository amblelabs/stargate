package dev.amble.stargate.api.event.block;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.StateResolveError;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record StargateBlockTickEvent(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) implements TEvent.Notify<StargateBlockEvents> {

    @Override
    public TEvents.BaseType<StargateBlockEvents> type() {
        return StargateBlockEvents.event;
    }

    @Override
    public void handle(StargateBlockEvents handler) throws StateResolveError {
        handler.block$tick(stargate, entity, world, pos, state);
    }

    public record Random(Stargate stargate, World world, BlockPos pos, BlockState state, net.minecraft.util.math.random.Random random) implements TEvent.Notify<StargateBlockEvents> {

        @Override
        public void handle(StargateBlockEvents handler) throws StateResolveError {
            handler.block$randomDisplayTick(stargate, world, pos, state, random);
        }

        @Override
        public TEvents.BaseType<StargateBlockEvents> type() {
            return StargateBlockEvents.event;
        }
    }
}
