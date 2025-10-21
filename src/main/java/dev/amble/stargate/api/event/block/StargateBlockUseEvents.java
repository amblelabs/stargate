package dev.amble.stargate.api.event.block;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface StargateBlockUseEvents extends TEvents {

    Type<StargateBlockUseEvents> event = new Type<>(StargateBlockUseEvents.class);

    boolean onUse(Stargate stargate, StargateBlockEntity blockEntity, PlayerEntity player, World world, BlockState state, BlockPos pos, Hand hand, BlockHitResult blockHit);
}
