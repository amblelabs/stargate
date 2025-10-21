package dev.amble.stargate.api.event.block;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.drtheo.yaar.event.TEvent;
import dev.drtheo.yaar.event.TEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class StargateBlockUseEvent implements TEvent.Result<StargateBlockUseEvents, Boolean> {

    private final Stargate stargate;
    private final StargateBlockEntity blockEntity;
    private final PlayerEntity player;
    private final World world;
    private final BlockState state;
    private final BlockPos pos;
    private final Hand hand;
    private final BlockHitResult blockHit;

    private boolean result;

    public StargateBlockUseEvent(Stargate stargate, StargateBlockEntity blockEntity, PlayerEntity player, World world, BlockState state, BlockPos pos, Hand hand, BlockHitResult blockHit) {
        this.stargate = stargate;
        this.blockEntity = blockEntity;
        this.player = player;
        this.world = world;
        this.state = state;
        this.pos = pos;
        this.hand = hand;
        this.blockHit = blockHit;
    }

    @Override
    public TEvents.BaseType<StargateBlockUseEvents> type() {
        return StargateBlockUseEvents.event;
    }

    @Override
    public Boolean result() {
        return result;
    }

    @Override
    public void handleAll(Iterable<StargateBlockUseEvents> iterable) {
        for (StargateBlockUseEvents events : iterable) {
            if (TEvent.handleSilent(this, events, () -> events.onUse(stargate, blockEntity, player, world, state, pos, hand, blockHit), false)) {
                this.result = true;
                return;
            }
        }
    }
}
