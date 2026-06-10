package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.common.blocks.DHDBlockEntity;
import dev.drtheo.ecs.event.TEvent;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.state.StateResolveError;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

public interface DHDBlockEvents extends TEvents {

    Type<DHDBlockEvents> type = new Type<>(DHDBlockEvents.class);

    void stargate$useItem(DHDBlockEntity dhd, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);

    void stargate$registerControllers(DHDBlockEntity dhd, AnimatableManager.ControllerRegistrar controllers);

    record UseItem(DHDBlockEntity dhd, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) implements TEvent.Notify<DHDBlockEvents> {

        @Override
        public BaseType<DHDBlockEvents> type() {
            return type;
        }

        @Override
        public void handle(DHDBlockEvents handler) throws StateResolveError {
            handler.stargate$useItem(dhd, itemStack, blockState, player, interactionHand, blockHitResult);
        }
    }

    record RegisterControllers(DHDBlockEntity dhd, AnimatableManager.ControllerRegistrar controllers) implements TEvent.Notify<DHDBlockEvents> {

        @Override
        public void handle(DHDBlockEvents handler) throws StateResolveError {
            handler.stargate$registerControllers(dhd, controllers);
        }

        @Override
        public BaseType<DHDBlockEvents> type() {
            return type;
        }
    }
}
