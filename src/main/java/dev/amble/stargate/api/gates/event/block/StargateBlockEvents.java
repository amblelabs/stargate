package dev.amble.stargate.api.gates.event.block;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public interface StargateBlockEvents extends TEvents {

    Type<StargateBlockEvents> event = new Type<>(StargateBlockEvents.class);

    default void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) { }

    default void block$randomDisplayTick(Stargate stargate, World world, BlockPos pos, BlockState state, Random random) { }
}
