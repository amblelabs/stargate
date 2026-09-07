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

import java.util.function.Consumer;

public interface StargateBlockEvents extends TEvents {

    Type<StargateBlockEvents> type = new Type<>(StargateBlockEvents.class);

    void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);
    void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult);
    void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random);

    static void notify(Consumer<StargateBlockEvents> handler) {
        TEvents.notify(type, handler);
    }

    interface Tick extends TEvents {
        Type<Tick> type = new Type<>(Tick.class);

        void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState);

        static void tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
            TEvents.notify(type, tick -> tick.stargate$tick(stargate, blockEntity, level, blockPos, blockState));
        }
    }

    interface Lifecycle extends TEvents {
        Type<Lifecycle> type = new Type<>(Lifecycle.class);

        void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos);
        void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston);

        static void place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos) {
            TEvents.notify(type, lifecycle -> lifecycle.stargate$place(stargate, blockEntity, state, level, pos));
        }

        static void broken(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston) {
            TEvents.notify(type, lifecycle -> lifecycle.stargate$break(stargate, blockEntity, state, level, pos, newState, movedByPiston));
        }
    }

    interface Animate extends TEvents {
        Type<Animate> type = new Type<>(Animate.class);

        void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers);

        static void registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {
            TEvents.notify(type, animate -> animate.stargate$registerControllers(stargate, blockEntity, controllers));
        }
    }
}
