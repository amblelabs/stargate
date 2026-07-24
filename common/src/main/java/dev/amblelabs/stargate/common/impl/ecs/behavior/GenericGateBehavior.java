package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.stargate.address.Glyph;
import dev.amblelabs.stargate.api.ecs.event.*;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.api.util.StargateUtil;
import dev.amblelabs.stargate.api.util.TeleportableEntity;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.ChevronState;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.impl.ecs.state.LevelState;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.drtheo.ecs.behavior.Resolve;
import dev.drtheo.ecs.behavior.TBehavior;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;
import java.util.Set;

public interface GenericGateBehavior {

    static void registerAll() {
        TBehaviorRegistry.register(Closed::new);
        TBehaviorRegistry.register(Opening::new);
        TBehaviorRegistry.register(Open::new);
    }

    class Closed implements TBehavior, StargateTickEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (!(stargate.state(GateState.state) instanceof GateState.Closed closed))
                return;

            if (stargate.isClient()) {
                if (closed.locking)
                    closed.timer = (closed.timer + 1) % calculateDelay(closed);

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

            if (!closed.locking || closed.timer++ < GateState.Closed.TICKS_PER_GLYPH2) return;

            closed.timer = 0;
            closed.locked++;

            StargateUtil.playSound(stargate, StargateSounds.CHEVRON_LOCK);
            stargate.setChanged();

            // TODO: add energy handling.
            AddressResolveEvent.Result resolved = TEvents.handle(new AddressResolveEvent(stargate, closed.address, closed.locked));

            if (!(resolved instanceof AddressResolveEvent.Result.Route route)) {
                // if FAILed *OR* PASSed through all resolvers with no result and the address length >= to max chevrons of this gate, then fail
                if (resolved instanceof AddressResolveEvent.Result.Fail || closed.locked >= stargate.state(ChevronState.state).chevrons)
                    this.fail(stargate);

                return;
            }

            manager.set(stargate, new GateState.Opening(route.stargate(), true));
            manager.set(route.stargate(), new GateState.Opening(null, false));
        }

        public static int calculateDelay(int curGlyph, int nextGlyph) {
            return Math.abs(nextGlyph - curGlyph) * GateState.Closed.TICKS_PER_GLYPH;
        }

        public static int calculateDelay(GateState.Closed closed) {
            char curGlyph = closed.locked != 0 ? closed.address.charAt(closed.locked - 1) : (char) (Glyph.ALL.length / 2);
            char nextGlyph = closed.address.charAt(closed.locked);

            return calculateDelay(
                    Glyph.charToIdx(curGlyph),
                    Glyph.charToIdx(nextGlyph)
            );
        }

        public void fail(Stargate stargate) {
            StargateUtil.playSound(stargate, StargateSounds.GATE_FAIL);
            manager.set(stargate, new GateState.Closed());
        }
    }

    class Opening implements TBehavior, StargateGateStateEvents, StargateTickEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (newState instanceof GateState.Opening)
                StargateUtil.playSound(stargate, StargateSounds.GATE_OPEN);
        }

        @Override
        public void tick(Stargate stargate) {
            if (!(stargate.state(GateState.state) instanceof GateState.Opening opening))
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

    class Open implements TBehavior, StargateTickEvents, StargateBlockEvents, StargateGateStateEvents {

        public static final AABB NS_DEFAULT = new AABB(-1, 0, 0, 1, 3, 0);
        public static final AABB WE_DEFAULT = new AABB(0, 0, -1, 0, 3, 1);

        @Resolve
        private final GateManagerBehavior manager = behavior();

        public void stargate$gateState(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (oldState instanceof GateState.Open
                    && newState instanceof GateState.Closed)
                StargateUtil.playSound(stargate, StargateSounds.GATE_CLOSE);
        }

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            if (!(stargate.state(GateState.state) instanceof GateState.Open open))
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

            LevelState targetPhys = target.resolveState(LevelState.state);

            StargateTpEvent.Result result = TEvents.handle(new StargateTpEvent(stargate, target, entity));
            if (result == StargateTpEvent.Result.DENY) return;

            BlockPos pos = stargate.resolveState(LevelState.state).pos;
            Vec3 offset = entity.position().subtract(pos.getCenter().subtract(0, 0.5, 0));

            StargateUtil.playSound(stargate, StargateSounds.GATE_TELEPORT);
            StargateUtil.playSound(target, StargateSounds.GATE_TELEPORT);

            // Retain entity velocity but reorient it towards the target stargate
            Vec3 velocity = entity.getDeltaMovement();
            Vec3 direction = targetPhys.pos.getCenter().subtract(pos.getCenter()).normalize();

            double speed = velocity.length();
            Vec3 newVelocity = direction.multiply(speed, speed, speed);
            Vec3 targetPos = targetPhys.pos.getCenter().add(offset);

            entity.teleportTo(targetPhys.level, targetPos.x, targetPos.y, targetPos.z,
                    Set.of(), entity.getYRot(), entity.getXRot());

            entity.setDeltaMovement(newVelocity);
            holder.stargate$setTicks(GateState.Open.TELEPORT_DELAY);
        }

        @Override
        public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
            if (stargate.isClient()) return;
            if (level.getGameTime() % GateState.Open.TELEPORT_FREQUENCY != 0) return;

            if (!(stargate.state(GateState.state) instanceof GateState.Open open))
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
        public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (random.nextInt(100) < 5) level.playSound(null, pos, StargateSounds.WORMHOLE_LOOP, SoundSource.BLOCKS);
        }

        @Override
        public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos) { }

        @Override
        public void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, ServerLevel level, BlockPos pos, BlockState newState, boolean movedByPiston) { }

        @Override
        public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {}

        @Override
        public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {}

        @Override
        public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {}
    }
}