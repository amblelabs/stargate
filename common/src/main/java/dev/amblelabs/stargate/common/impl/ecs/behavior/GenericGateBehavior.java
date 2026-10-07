package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.stargate.address.Glyph;
import dev.amblelabs.stargate.api.ecs.event.*;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.SoundUtil;
import dev.amblelabs.stargate.api.util.StargateUtil;
import dev.amblelabs.stargate.api.util.TeleportableEntity;
import dev.amblelabs.stargate.client.renderers.layers.GlyphRenderLayer;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.ChevronState;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import dev.amblelabs.stargate.common.lib.StargateAdvancementTriggers;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.drtheo.ecs.behavior.Resolve;
import dev.drtheo.ecs.behavior.TBehavior;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEvents;
import dev.drtheo.ecs.event.TResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;

public interface GenericGateBehavior {

    static void registerAll() {
        TBehaviorRegistry.register(Closed::new);
        TBehaviorRegistry.register(Opening::new);
        TBehaviorRegistry.register(Open::new);
    }

    class Closed implements TBehavior {

        public static final RawAnimation LOCK_SYMBOL = RawAnimation.begin().thenPlay("LOCK_SYMBOL");

        @Resolve
        private final GateManagerBehavior manager = behavior();

        public Closed() {
            subscribe(StargateEvents.Tick.event, this::tick);
            subscribe(StargateEvents.Animate.event, this::onRegisterAnimations);
        }

        public void tick(Stargate stargate) {
            if (!(manager.get(stargate) instanceof GateState.Closed closed))
                return;

            if (stargate.isClient()) {
                if (closed.locking) closed.timer++;
                return;
            }

            int length = closed.address.length();
            closed.locking = length > closed.locked;

            // fixup broken state
            if (length < closed.locked) {
                closed.locked = length;
                closed.timer = 0;

                stargate.setChanged();
                return;
            }

            if (!closed.locking || closed.timer++ < calculateDelay(closed)) return;

            closed.timer = 0;
            closed.locked++;

            boolean _ = StargateUtil.playSound(stargate, StargateSounds.CHEVRON_LOCK);
            stargate.setChanged();

            // TODO: add energy handling.
            AddressResolveEvent.Result resolved = TEvents.handle(new AddressResolveEvent(stargate, closed.address, closed.locked));

            if (!(resolved instanceof AddressResolveEvent.Result.Route route)) {
                // if FAILed *OR* PASSed through all resolvers with no result and the address length >= to max chevrons of this gate, then fail
                if (resolved instanceof AddressResolveEvent.Result.Fail || closed.locked >= stargate.state(ChevronState.type).chevrons())
                    this.fail(stargate);

                return;
            }

            for (Player nearbyPlayer : StargateUtil.getPlayersNearby(stargate, 16)) {
                StargateAdvancementTriggers.DIAL.get().trigger((ServerPlayer) nearbyPlayer, closed.locked);
            }

            for (Player nearbyPlayer : StargateUtil.getPlayersNearby(route.stargate(), 16)) {
                StargateAdvancementTriggers.DIAL.get().trigger((ServerPlayer) nearbyPlayer, closed.locked);
            }

            manager.set(stargate, new GateState.Opening(route.stargate(), true));
            manager.set(route.stargate(), new GateState.Opening(null, false));
        }

        public static int calculateDelay(int curGlyph, int nextGlyph) {
            return Math.abs(nextGlyph - curGlyph) * GateState.Closed.TICKS_PER_GLYPH + GateState.Closed.EXTRA_TICKS_PER_GLYPH;
        }

        public static int calculateDelay(GateState.Closed closed) {
            if (closed.address.length() <= closed.locked) return 0;

            char curGlyph = closed.locked != 0 ? closed.address.charAt(closed.locked - 1) : (char) (Glyph.ALL.length / 2);
            char nextGlyph = closed.address.charAt(closed.locked);

            return calculateDelay(
                    GlyphRenderLayer.ALPHABET.indexOf(curGlyph),
                    GlyphRenderLayer.ALPHABET.indexOf(nextGlyph)
            );
        }

        public void fail(Stargate stargate) {
            boolean _ = StargateUtil.playSound(stargate, StargateSounds.GATE_FAIL);
            manager.set(stargate, new GateState.Closed());
        }

        public void onRegisterAnimations(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(blockEntity, "Lock",
                    anim -> {
                        GateState<?> state = manager.get(stargate);
                        if (state instanceof GateState.Closed closed && closed.locking
                                && closed.timer >= calculateDelay(closed) - GateState.Closed.EXTRA_TICKS_PER_GLYPH)
                            return anim.setAndContinue(LOCK_SYMBOL);

                        anim.resetCurrentAnimation();
                        return PlayState.STOP;
                    }));
        }
    }

    class Opening implements TBehavior, StargateGateStateEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        public Opening() {
            subscribe(StargateEvents.Tick.event, this::tick);
        }

        @Override
        public void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (newState instanceof GateState.Opening) {
                boolean _ = StargateUtil.playSound(stargate, StargateSounds.GATE_OPEN);
            }
        }

        public void tick(Stargate stargate) {
            if (!(manager.get(stargate) instanceof GateState.Opening opening))
                return;

            if (opening.timer++ <= GateState.Opening.TICKS_PER_KAWOOSH) return;

            if (stargate.isClient()) return;

            // Handle missing gates by address gracefully
            if (opening.caller && opening.target != null) {
                // TODO: add distance/protocol compat checks here
                manager.set(stargate, new GateState.Open(opening.target, true));
                manager.set(opening.target, new GateState.Open(stargate, false));
            } else {
                manager.set(stargate, new GateState.Closed());
            }
        }
    }

    class Open implements TBehavior, StargateBlockEvents, StargateGateStateEvents {

        public static final AABB NS_DEFAULT = new AABB(-1, 0, 0, 1, 3, 0);
        public static final AABB WE_DEFAULT = new AABB(0, 0, -1, 0, 3, 1);

        @Resolve
        private final GateManagerBehavior manager = behavior();

        public Open() {
            subscribe(StargateEvents.Tick.event, this::tick);
            subscribe(StargateEvents.BlockTick.event, this::blockTick);
        }

        public void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (oldState instanceof GateState.Open
                    && newState instanceof GateState.Closed) {
                boolean _ = StargateUtil.playSound(stargate, StargateSounds.GATE_CLOSE);
            }
        }

        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            if (!(manager.get(stargate) instanceof GateState.Open open))
                return;

            // handle abnormal state
            if (open.target == null) {
                manager.set(stargate, new GateState.Closed());
                return;
            }

            if (open.timer++ > GateState.Open.TICKS_PER_OPEN) {
                open.timer = 0;

                manager.set(stargate, new GateState.Closed());
                manager.set(open.target, new GateState.Closed());
            }
        }

        public void tryTeleportFrom(Stargate stargate, GateState.Open open, LivingEntity entity) {
            if (!(entity instanceof TeleportableEntity holder) || holder.stargate$updateAndGetTicks(GateState.Open.TELEPORT_DELAY) != 0)
                return;

            Stargate target = open.target;
            if (target == null) return; // this is most likely false, since we do a check every tick, but just in case...

            LevelState sourcePhys = stargate.resolveState(LevelState.type);
            LevelState targetPhys = target.resolveState(LevelState.type);

            TResult result = StargateTpEvent.type.invoker().stargate$tp(stargate, target, entity);
            if (result == TResult.DENY) return;

            BlockPos pos = sourcePhys.pos();
            Vec3 offset = entity.position().subtract(pos.getCenter());

            StargateUtil.playSound(sourcePhys, StargateSounds.GATE_TELEPORT);
            StargateUtil.playSound(targetPhys, StargateSounds.GATE_TELEPORT);

            Vec3 targetPos = targetPhys.pos().getCenter().add(offset);

            // gets source gate rotation - addie
            BlockState sourceState = StargateUtil.getBlockState(sourcePhys);
            Direction.Axis sourceAxis = sourceState.getValue(StargateBlock.FACING).getAxis();

            // gets target gate rotation - addie
            BlockState targetState = StargateUtil.getBlockState(targetPhys);
            Direction.Axis targetAxis = targetState.getValue(StargateBlock.FACING).getAxis();
            Direction targetFacing = targetState.getValue(StargateBlock.FACING);

            float yRot = entity.getYRot();
            float xRot = entity.getXRot();

            boolean axisMismatched = sourceAxis.isHorizontal()
                    && targetAxis.isHorizontal()
                    && sourceAxis != targetAxis;

            if (axisMismatched){
                yRot += 90.0F;
            }

            // flips N and S cus it was flipped before (i blame loqor :3) - addie
            if (targetFacing.getAxis() == Direction.Axis.Z) {
                yRot += 180.0F;
            }

            Vec3 motion = entity.getDeltaMovement();

            entity.teleportTo(targetPhys.level(), targetPos.x, targetPos.y, targetPos.z,
                    RelativeMovement.ALL, yRot, xRot);

            entity.setDeltaMovement(motion);

            holder.stargate$setTicks(GateState.Open.TELEPORT_DELAY);

            // TODO: post-tp event
            if (entity instanceof ServerPlayer serverPlayer)
                StargateAdvancementTriggers.PASSED_THROUGH.get().trigger(serverPlayer);
        }

        public void blockTick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
            if (stargate.isClient()) return;
            if (level.getGameTime() % GateState.Open.TELEPORT_FREQUENCY != 0) return;

            if (!(manager.get(stargate) instanceof GateState.Open open))
                return;

            Direction facing = blockState.getValue(StargateBlock.FACING);

            AABB aabb = facing.getAxis() == Direction.Axis.Z ? NS_DEFAULT : WE_DEFAULT;
            aabb = aabb.move(blockPos);

            List<Entity> entities = level.getEntitiesOfClass(Entity.class, aabb, e -> e.isAlive() && !e.isSpectator());

            for (Entity e : entities) {
                if (e instanceof LivingEntity living)
                    tryTeleportFrom(stargate, open, living);
            }
        }

        @Override
        public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) { }

        @Override
        public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) { }

        @Override
        public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            SoundUtil.playSound(level, pos, StargateSounds.WORMHOLE_LOOP, SoundSource.BLOCKS, 0.5f);
        }
    }
}