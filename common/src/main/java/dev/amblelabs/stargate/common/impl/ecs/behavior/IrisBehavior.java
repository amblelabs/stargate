package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.IrisEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateTpEvent;
import dev.amblelabs.stargate.api.ecs.event.StargateTpEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.SoundUtil;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.lib.StargateDamageTypes;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class IrisBehavior implements TBehavior, StargateBlockEvents, StargateTpEvents {

    public static final RawAnimation IRIS_OPEN = RawAnimation.begin().thenPlay("IRIS_OPEN");
    public static final RawAnimation IRIS_CLOSE = RawAnimation.begin().thenPlay("IRIS_CLOSE");

    public void damage(Stargate stargate, int amount) {
        IrisState iris = stargate.state(IrisState.state);
        boolean broken = (iris.durability -= amount) <= 0;

        if (broken) {
            handle(new IrisEvents.Broken(stargate, iris));
            stargate.removeState(IrisState.state);
        }

        stargate.setChanged();

        if (broken) {
            LevelState globalPos = stargate.state(LevelState.state);
            globalPos.level.playSound(null, globalPos.pos, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS);
        }
    }

    @Override
    public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevelAccessor level, BlockPos pos) { }

    @Override
    public void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston) { }

    @Override
    public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) { }

    @Override
    public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.getItem() instanceof IrisItem iris && !stargate.hasState(IrisState.state)) {
            stargate.addState(iris.toState());
            player.getItemInHand(interactionHand).consume(1, player);
        }
    }

    @Override
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        IrisState iris = stargate.state(IrisState.state);
        iris.closed = !iris.closed;

        stargate.setChanged();

        LevelState globalPos = stargate.state(LevelState.state);
        SoundUtil.playSound(globalPos.level, globalPos.pos,
                iris.closed ? StargateSounds.IRIS_CLOSE : StargateSounds.IRIS_OPEN, SoundSource.BLOCKS);
    }

    @Override
    public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { }

    @Override
    public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(blockEntity, "Iris",
                anim -> {
                    IrisState state = stargate.stateOrNull(IrisState.state);
                    return anim.setAndContinue(state == null || !state.closed ? IRIS_OPEN : IRIS_CLOSE);
                }));
    }

    @Override
    public StargateTpEvent.Result onGateTp(Stargate from, Stargate to, Entity entity) {
        IrisState iris = to.state(IrisState.state);

        if (!iris.closed)
            return StargateTpEvent.Result.PASS;

        LevelState globalPos = to.state(LevelState.state);
        Level targetWorld = globalPos.level;

        entity.hurt(StargateDamageTypes.source(targetWorld, StargateDamageTypes.IRIS), Integer.MAX_VALUE);
        SoundUtil.playSound(targetWorld, globalPos.pos, StargateSounds.IRIS_HIT, SoundSource.BLOCKS);

        this.damage(to, 5); // TODO: scale the amount
        return StargateTpEvent.Result.DENY;
    }
}
