package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

// TODO: use EventSingle
public interface StargateBlockEvents extends TEvents {

    EventGroup<StargateBlockEvents> type = new EventGroup<>(StargateBlockEvents.class);

    void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);
    void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult);
    void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random);

    @FunctionalInterface
    interface Tick extends TEvents {
        EventSingle<Tick> event = new EventSingle<>(Tick.class);

        void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState);
    }

    @FunctionalInterface
    interface Break extends TEvents {
        EventSingle<Break> event = new EventSingle<>(Break.class);

        void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston);
    }

    @FunctionalInterface
    interface Place extends TEvents {
        EventSingle<Place> event = new EventSingle<>(Place.class);

        void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos);
    }

    @FunctionalInterface
    interface Animate extends TEvents {
        EventSingle<Animate> event = new EventSingle<>(Animate.class);

        void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers);
    }
}
