package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.IrisEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class IrisBehavior implements TBehavior, StargateBlockEvents {

    public static final RawAnimation IRIS_OPEN = RawAnimation.begin().thenPlay("IRIS_OPEN");
    public static final RawAnimation IRIS_CLOSE = RawAnimation.begin().thenPlay("IRIS_CLOSE");

    public void damage(StargateBlockEntity stargate, int amount) {
        IrisState iris = stargate.stargate.state(IrisState.state);

        iris.durability -= amount;

        if (iris.durability <= 0) {
            handle(new IrisEvents.Broken(stargate, iris));
            stargate.stargate.removeState(IrisState.state);
        }

        stargate.setChanged();
    }

    @Override
    public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.getItem() instanceof IrisItem iris && !stargate.hasState(IrisState.state)) {
            stargate.addState(iris.toState());
            player.getItemInHand(interactionHand).consume(1, player);
        }
    }

    @Override
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Player player, BlockHitResult blockHitResult) {
        IrisState iris = stargate.state(IrisState.state);
        iris.closed = !iris.closed;
        blockEntity.setChanged();
    }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(blockEntity, "Iris",
                anim -> {
                    IrisState state = stargate.stateOrNull(IrisState.state);
                    return anim.setAndContinue(state == null || !state.closed ? IRIS_OPEN : IRIS_CLOSE);
                }));
    }
}
