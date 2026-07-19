package dev.amblelabs.stargate.api.ecs.event;

import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.function.Consumer;

public interface StargateBlockEvents extends TEvents {

    Type<StargateBlockEvents> type = new Type<>(StargateBlockEvents.class);

    void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, ServerLevel level, BlockPos blockPos, BlockState blockState);
    void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState);
    void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult);
    void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Player player, BlockHitResult blockHitResult);

    void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers);

    static void notify(Consumer<StargateBlockEvents> handler) {
        TEvents.notify(type, handler);
    }
}
