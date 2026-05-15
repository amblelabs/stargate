package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.event.TEvent;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.StateResolveError;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

public interface StargateBlockEvents extends TEvents {

    Type<StargateBlockEvents> type = new Type<>(StargateBlockEvents.class);

    void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);

    void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers);

    record UseItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) implements TEvent.Notify<StargateBlockEvents> {

        @Override
        public BaseType<StargateBlockEvents> type() {
            return type;
        }

        @Override
        public void handle(StargateBlockEvents handler) throws StateResolveError {
            handler.stargate$useItem(stargate, blockEntity, itemStack, blockState, player, interactionHand, blockHitResult);
        }
    }

    record RegisterControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) implements TEvent.Notify<StargateBlockEvents> {

        @Override
        public void handle(StargateBlockEvents handler) throws StateResolveError {
            handler.stargate$registerControllers(stargate, blockEntity, controllers);
        }

        @Override
        public BaseType<StargateBlockEvents> type() {
            return type;
        }
    }
}
