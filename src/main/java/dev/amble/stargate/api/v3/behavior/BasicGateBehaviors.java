package dev.amble.stargate.api.v3.behavior;

import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.api.TeleportableEntity;
import dev.amble.stargate.api.network.StargateRef;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.StargateTpEvent;
import dev.amble.stargate.api.v3.event.address.AddressResolveEvent;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.Resolve;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
import dev.drtheo.yaar.state.TState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public interface BasicGateBehaviors {

    class Closed implements TBehavior, StargateEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            BasicGateStates.Closed closed = stargate.state(BasicGateStates.Closed.state);

            int length = closed.address.length();
            closed.locking = length > closed.locked;

            // fixup broken state
            if (length < closed.locked) {
                closed.locked = length;
                closed.timer = 0;

                stargate.markDirty();
                return;
            }

            if (!closed.locking || closed.timer++ < BasicGateStates.Closed.TICKS_PER_CHEVRON) return;

            closed.timer = 0;
            closed.locked++;

            stargate.playSound(StargateSounds.CHEVRON_LOCK);
            stargate.markDirty();

            AddressResolveEvent.Result resolved = TEvents.handle(new AddressResolveEvent(stargate, closed.address));

            // TODO: add energy handling.
            resolved.ifRoute(route -> {
                manager.set(stargate, new BasicGateStates.Opening(new StargateRef(route.stargate()), true));
                manager.set(route.stargate(), new BasicGateStates.Opening(null, false));

                route.stargate().markDirty();
            }).ifFail(() -> {
                stargate.playSound(StargateSounds.GATE_FAIL);
                manager.set(stargate, new BasicGateStates.Closed());
            });
        }

    }

    class Opening implements TBehavior, StargateEvents {

        static final float p0 = 0f;
        static final float p1 = 22f;
        static final float p2 = -12f;
        static final float p3 = 0;

        @Override
        public void onStateChanged(Stargate stargate, TState<?> oldState, TState<?> newState) {
            if (!(newState instanceof BasicGateStates.Opening)) return;

            stargate.playSound(StargateSounds.GATE_OPEN);
        }

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            BasicGateStates.Opening opening = stargate.state(BasicGateStates.Opening.state);

            // Adjust Bezier control points and t-mapping to linger longer near p1 and p2
            float t = (float) opening.timer / ((float) BasicGateStates.Opening.TICKS_PER_KAWOOSH * 1.25f);

            // Remap t to ease in and out, spending more time near p1 and p2
            // Use a custom curve: t' = 3t^2 - 2t^3 (smoothstep), then stretch the middle
            float tPrime = MathHelper.clamp((float) (3 * Math.pow(t, 2) - 2 * Math.pow(t, 3)), 0, 1);

            opening.kawooshHeight = (float) (
                    Math.pow(1 - tPrime, 3) * p0 +
                            3 * Math.pow(1 - tPrime, 2) * tPrime * p1 +
                            3 * (1 - tPrime) * Math.pow(tPrime, 2) * p2 +
                            Math.pow(tPrime, 3) * p3
            );

            stargate.markDirty();

            if (opening.timer++ <= BasicGateStates.Opening.TICKS_PER_KAWOOSH || tPrime != 1) return;

            opening.kawooshHeight = 0;
            opening.timer = 0;

            // Handle missing gates by address gracefully
            if (opening.caller && opening.target != null) { // TODO: add distance/protocol compat checks here
                manager.set(stargate, new BasicGateStates.Open(opening.target, true));
                manager.set(opening.target.get(), new BasicGateStates.Open(new StargateRef(stargate), false));
            } else {
                manager.set(stargate, new BasicGateStates.Closed());
            }
        }
    }

    class Open implements TBehavior, StargateEvents, StargateBlockEvents {

        public static final Box NS_DEFAULT = new Box(BlockPos.ORIGIN).expand(2, 2, 0).expand(0, 3, 0);
        public static final Box WE_DEFAULT = new Box(BlockPos.ORIGIN).expand(0, 2, 2).expand(0, 3, 0);

        @Resolve
        private final GateManagerBehavior manager = behavior();

        private final Box northSouth;
        private final Box westEast;

        public Open(Box northSouth, Box westEast) {
            this.northSouth = northSouth;
            this.westEast = westEast;
        }

        public Open() {
            this(NS_DEFAULT, WE_DEFAULT);
        }

        @Override
        public void onStateChanged(Stargate stargate, TState<?> oldState, TState<?> newState) {
            if (oldState instanceof BasicGateStates.Open && newState instanceof BasicGateStates.Closed) {
                stargate.playSound(StargateSounds.GATE_CLOSE);
            }
        }

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            BasicGateStates.Open open = stargate.state(BasicGateStates.Open.state);

            // handle abnormal state
            if (open.target.isEmpty()) {
                manager.set(stargate, new BasicGateStates.Closed());
                return;
            }

            if (open.timer++ > BasicGateStates.Open.TICKS_PER_OPEN) {
                open.timer = 0;

                manager.set(stargate, new BasicGateStates.Closed());
                manager.set(open.target.get(), new BasicGateStates.Closed());
            }
        }

        @Override
        public void block$randomDisplayTick(Stargate stargate, World world, BlockPos pos, BlockState state, Random random) {
            if (random.nextInt(100) < 5) {
                world.playSound(null, pos, StargateSounds.WORMHOLE_LOOP, SoundCategory.BLOCKS);
            }
        }

        @Override
        public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
            if (world.isClient()) return;

            Direction facing = state.get(StargateBlock.FACING);

            Box box = facing == Direction.WEST || facing == Direction.EAST ? westEast : northSouth;
            box = box.offset(pos);

            if (ServerLifecycleHooks.get().getTicks() % BasicGateStates.Open.TELEPORT_FREQUENCY == 0) {
                BasicGateStates.Open open = stargate.state(BasicGateStates.Open.state);
                List<Entity> entities = world.getOtherEntities(null, box, e -> e != null && e.isAlive() && !e.isSpectator());

                for (Entity e : entities) {
                    if (e instanceof LivingEntity living)
                        tryTeleportFrom(stargate, open, living);
                }
            }
        }

        public void tryTeleportFrom(Stargate stargate, BasicGateStates.Open open, LivingEntity entity) {
            if (!(entity instanceof TeleportableEntity holder))
                return;

            Stargate target = open.target.get();

            // this is most likely false, since we do a check every tick, but just in case...
            if (target == null)
                return;

            ServerWorld targetWorld = (ServerWorld) target.world();
            BlockPos targetBlockPos = target.pos();

            StargateTpEvent.Result result = TEvents.handle(new StargateTpEvent(stargate, target, entity));

            if (result == StargateTpEvent.Result.DENY)
                return;

            BlockPos pos = stargate.pos();
            Vec3d offset = entity.getPos().subtract(pos.toCenterPos().subtract(0, 0.5, 0));

            double yOffset = 0;
            for (int y = 1; y <= 5; y++) {
                boolean bottomIsAir = targetWorld.getBlockState(targetBlockPos.up(y)).isAir();
                boolean belowIsAir = targetWorld.getBlockState(targetBlockPos.up(y - 1)).isAir();
                if (!bottomIsAir || !belowIsAir) {
                    yOffset = y;
                    break;
                }
            }

            entity.getWorld().playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
            targetWorld.playSound(null, targetBlockPos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

            // Check for blocks above the target position and adjust the Y offset accordingly
            // Retain entity velocity but reorient it towards the target stargate
            Vec3d velocity = entity.getVelocity();
            Vec3d direction = targetBlockPos.toCenterPos().subtract(pos.toCenterPos()).normalize();

            double speed = velocity.length();
            Vec3d newVelocity = direction.multiply(speed);

            TeleportUtil.teleport(entity, targetWorld,
                    targetBlockPos.toCenterPos().add(offset).add(0, yOffset, 0),
                    target.direction().asRotation()
            );

            entity.setVelocity(newVelocity);
            holder.stargate$setTicks(BasicGateStates.Open.TELEPORT_DELAY);
        }
    }
}
