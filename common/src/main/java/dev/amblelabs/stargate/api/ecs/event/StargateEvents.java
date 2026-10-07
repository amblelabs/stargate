package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.ecs.NbtDeserializer;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimatableManager;

public interface StargateEvents {

    @FunctionalInterface
    interface Instantiate extends TEvents {
        EventSingle<Instantiate> event = new EventSingle<>(Instantiate.class);

        void onInstantiated(Stargate stargate, NbtDeserializer.Context ctx);
    }

    @FunctionalInterface
    interface Tick extends TEvents {
        EventSingle<Tick> event = new EventSingle<>(Tick.class);

        void tick(Stargate stargate);
    }

    @FunctionalInterface
    interface BlockTick extends TEvents {
        EventSingle<BlockTick> event = new EventSingle<>(BlockTick.class);

        void blockTick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState);
    }

    @FunctionalInterface
    interface Break extends TEvents {
        EventSingle<Break> event = new EventSingle<>(Break.class);

        void onBreak(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston);
    }

    @FunctionalInterface
    interface Place extends TEvents {
        EventSingle<Place> event = new EventSingle<>(Place.class);

        void onPlace(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos);
    }

    @FunctionalInterface
    interface Animate extends TEvents {
        EventSingle<Animate> event = new EventSingle<>(Animate.class);

        void onRegisterAnimations(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers);
    }
}
