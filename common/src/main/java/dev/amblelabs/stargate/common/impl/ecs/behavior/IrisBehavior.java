package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.IrisEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateTpEvent;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.SoundUtil;
import dev.amblelabs.stargate.api.util.StargateUtil;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.IrisState;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import dev.amblelabs.stargate.common.items.IrisItem;
import dev.amblelabs.stargate.common.lib.StargateAdvancementTriggers;
import dev.amblelabs.stargate.common.lib.StargateDamageTypes;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.drtheo.ecs.behavior.TBehavior;
import dev.drtheo.ecs.event.TResult;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class IrisBehavior implements TBehavior, StargateBlockEvents {

    public static final RawAnimation IRIS_OPEN = RawAnimation.begin().thenPlay("IRIS_OPEN");
    public static final RawAnimation IRIS_CLOSE = RawAnimation.begin().thenPlay("IRIS_CLOSE");

    @Override
    public void initialize() {
        subscribe(StargateTpEvent.type, this::onGateTp);
        subscribe(StargateBlockEvents.Animate.event, this::registerAnimations);
    }

    public void damage(Stargate stargate, int amount) {
        IrisState iris = stargate.state(IrisState.type);
        boolean broken = (iris.durability -= amount) <= 0;

        if (broken) {
            handle(new IrisEvents.Broken(stargate, iris));
            stargate.removeState(IrisState.type);
        }

        stargate.setChanged();

        if (broken) {
            boolean _ = StargateUtil.playSound(stargate, SoundEvents.CHAIN_BREAK);

            // TODO: iris break event
            for (Player nearbyPlayer : StargateUtil.getPlayersNearby(stargate, 16)) {
                // TODO: award stat
                StargateAdvancementTriggers.BREAK_IRIS.get().trigger((ServerPlayer) nearbyPlayer, iris);
            }
        }
    }

    @Override
    public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!(itemStack.getItem() instanceof IrisItem iris) || stargate.hasState(IrisState.type)) return;

        // failed to give the iris state
        if (!stargate.addState(iris.toState())) return;

        player.getItemInHand(interactionHand).consume(1, player);

        this.damage(stargate, 0); // force break check just in case
    }

    @Override
    public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        IrisState iris = stargate.state(IrisState.type);
        iris.closed = !iris.closed;

        stargate.setChanged();

        LevelState globalPos = stargate.state(LevelState.type);
        SoundUtil.playSound(globalPos.level(), globalPos.pos(),
                iris.closed ? StargateSounds.IRIS_CLOSE : StargateSounds.IRIS_OPEN, SoundSource.BLOCKS);
    }

    @Override
    public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { }

    public void registerAnimations(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(blockEntity, "Iris",
                anim -> {
                    IrisState state = stargate.stateOrNull(IrisState.type);
                    return anim.setAndContinue(state == null || !state.closed ? IRIS_OPEN : IRIS_CLOSE);
                }));
    }

    public TResult onGateTp(Stargate from, Stargate to, Entity entity) {
        IrisState iris = to.state(IrisState.type);

        if (!iris.closed)
            return TResult.PASS;

        LevelState phys = to.state(LevelState.type);

        entity.hurt(StargateDamageTypes.source(phys.level(), StargateDamageTypes.IRIS), Integer.MAX_VALUE);

        if (entity instanceof ServerPlayer serverPlayer)
            StargateAdvancementTriggers.IRIS_DAMAGE.get().trigger(serverPlayer);

        StargateUtil.playSound(phys, StargateSounds.IRIS_HIT);
        this.damage(to, 5); // TODO: scale the amount

        return TResult.DENY;
    }
}
