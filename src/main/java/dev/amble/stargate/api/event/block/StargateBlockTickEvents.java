package dev.amble.stargate.api.event.block;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public interface StargateBlockTickEvents extends TEvents {

    Type<StargateBlockTickEvents> event = new Type<>(StargateBlockTickEvents.class);

    void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state);

    default void block$randomDisplayTick(Stargate stargate, World world, BlockPos pos, BlockState state, Random random) { }
}
