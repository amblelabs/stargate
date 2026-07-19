package dev.amblelabs.stargate.common.impl.ecs.behavior;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.ecs.event.StargateTickEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.lib.StargateSounds;
import dev.drtheo.ecs.behavior.Resolve;
import dev.drtheo.ecs.behavior.TBehavior;
import dev.drtheo.ecs.behavior.TBehaviorRegistry;
import dev.drtheo.ecs.event.TEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
            GateState.Closed closed = stargate.state(GateState.Closed.state);

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

                stargate.markDirty();
                return;
            }

            if (!closed.locking || closed.timer++ < GateState.Closed.TICKS_PER_GLYPH2) return;

            closed.timer = 0;
            closed.locked++;

            stargate.playSound(StargateSounds.CHEVRON_LOCK);
            stargate.markDirty();

            // TODO: add energy handling.
            AddressResolveEvent.Result resolved = TEvents.handle(new AddressResolveEvent(stargate, closed.address, closed.locked));

            if (!(resolved instanceof AddressResolveEvent.Result.Route route)) {
                // if FAILed *OR* PASSed through all resolvers with no result and the address length >= to max chevrons of this gate, then fail
                if (resolved instanceof AddressResolveEvent.Result.Fail || closed.locked >= stargate.kernel().maxChevrons)
                    this.fail(stargate);

                return;
            }

            manager.set(stargate, new GateState.Opening(route.stargate(), true));
            manager.set(route.stargate(), new GateState.Opening(null, false));

            route.stargate().markDirty();
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
            stargate.playSound(StargateSounds.GATE_FAIL);
            manager.set(stargate, new GateState.Closed());
        }
    }

    class Opening implements TBehavior, StargateGateStateEvents, StargateTickEvents {

        static final float p0 = 0;
        static final float p1 = 22;
        static final float p2 = -12;
        static final float p3 = 0;

        @Override
        public void onStateChanged(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (newState.gateState() == GateState.StateType.OPENING)
                stargate.playSound(StargateSounds.GATE_OPEN);
        }

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            GateState.Opening opening = stargate.state(GateState.Opening.state);

            // Adjust Bezier control points and t-mapping to linger longer near p1 and p2
            float t = (float) opening.timer / (GateState.Opening.TICKS_PER_KAWOOSH * 1.25f);

            // Remap t to ease in and out, spending more time near p1 and p2
            // Use a custom curve: t' = 3t^2 - 2t^3 (smoothstep), then stretch the middle
            float tPrime = Mth.clamp((float) (3 * Math.pow(t, 2) - 2 * Math.pow(t, 3)), 0, 1);

            opening.kawooshHeight = (float) (
                    Math.pow(1 - tPrime, 3) * p0 +
                            3 * Math.pow(1 - tPrime, 2) * tPrime * p1 +
                            3 * (1 - tPrime) * Math.pow(tPrime, 2) * p2 +
                            Math.pow(tPrime, 3) * p3
            );

            if (opening.timer++ <= GateState.Opening.TICKS_PER_KAWOOSH || tPrime != 1) return;

            opening.kawooshHeight = 0;
            opening.timer = 0;

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

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void onStateChanged(Stargate stargate, GateState<?> oldState, GateState<?> newState) {
            if (oldState.gateState() == GateState.StateType.OPEN
                    && newState.gateState() == GateState.StateType.CLOSED)
                stargate.playSound(StargateSounds.GATE_CLOSE);
        }

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            GateState.Open open = stargate.state(GateState.Open.state);

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

            BlockPos targetBlockPos = target.pos();
            ServerLevel targetLevel = target.world();

            StargateTpEvent.Result result = TEvents.handle(new StargateTpEvent(stargate, target, entity));
            if (result == StargateTpEvent.Result.DENY) return;

            BlockPos pos = stargate.pos();
            Vec3 offset = entity.position().subtract(pos.getCenter().subtract(0, 0.5, 0));

            entity.level().playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundSource.BLOCKS, 1f, 1);
            targetLevel.playSound(null, targetBlockPos, StargateSounds.GATE_TELEPORT, SoundSource.BLOCKS, 1f, 1);

            // Retain entity velocity but reorient it towards the target stargate
            Vec3 velocity = entity.getDeltaMovement();
            Vec3 direction = targetBlockPos.getCenter().subtract(pos.getCenter()).normalize();

            double speed = velocity.length();
            Vec3 newVelocity = direction.multiply(speed, speed, speed);

            TeleportUtil.teleport(entity, targetLevel,
                    targetBlockPos.getCenter().add(offset),
                    target.facing().asRotation()
            );

            entity.setDeltaMovement(newVelocity);
            holder.stargate$setTicks(GateState.Open.TELEPORT_DELAY);
        }

        @Override
        public void stargate$tick(Stargate stargate, StargateBlockEntity blockEntity, Level level, BlockPos blockPos, BlockState blockState) {
            if (someGate.isClient()) return;
            if (level.getGameTime() % GateState.Open.TELEPORT_FREQUENCY != 0) return;

            Direction facing = state.getValue(StargateBlock.FACING);
            AABB aabb = someGate.kernel().forDirection(facing).offset(pos);

            GateState.Open open = someGate.state(GateState.Open.state);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, aabb, e -> e.isAlive() && !e.isSpectator());

            for (Entity e : entities) {
                if (e instanceof LivingEntity living)
                    tryTeleportFrom(someGate, open, living);
            }
        }

        @Override
        public void stargate$randomTick(Stargate stargate, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (random.nextInt(100) < 5) {
                level.playSound(null, pos, StargateSounds.WORMHOLE_LOOP, SoundSource.BLOCKS);
            }
        }

        @Override
        public void stargate$place(Stargate stargate, StargateBlockEntity blockEntity, ServerLevel level, BlockPos blockPos, BlockState blockState) {}

        @Override
        public void stargate$break(Stargate stargate, StargateBlockEntity blockEntity, BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {}

        @Override
        public void stargate$useItem(Stargate stargate, StargateBlockEntity blockEntity, ItemStack itemStack, BlockState blockState, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {}

        @Override
        public void stargate$use(Stargate stargate, StargateBlockEntity blockEntity, BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {}

        @Override
        public void stargate$registerControllers(Stargate stargate, StargateBlockEntity blockEntity, AnimatableManager.ControllerRegistrar controllers) {}
    }
}