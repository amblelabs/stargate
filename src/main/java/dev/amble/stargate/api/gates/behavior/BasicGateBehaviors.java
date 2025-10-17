package dev.amble.stargate.api.gates.behavior;

import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.api.util.TeleportableEntity;
import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.event.tick.StargateTickEvents;
import dev.amble.stargate.api.gates.event.tp.StargateTpEvent;
import dev.amble.stargate.api.gates.event.address.AddressResolveEvent;
import dev.amble.stargate.api.gates.event.block.StargateBlockEvents;
import dev.amble.stargate.api.gates.event.state.gate.StargateGateStateEvents;
import dev.amble.stargate.api.gates.state.GateState;
import dev.amble.stargate.api.gates.state.stargate.GateIdentityState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.Resolve;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.event.TEvents;
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

    class Closed implements TBehavior, StargateTickEvents {

        @Resolve
        private final GateManagerBehavior manager = behavior();

        @Override
        public void tick(Stargate stargate) {
            if (stargate.isClient()) return;

            GateState.Closed closed = stargate.state(GateState.Closed.state);

            int length = closed.address.length();
            closed.locking = length > closed.locked;

            // fixup broken state
            if (length < closed.locked) {
                closed.locked = length;
                closed.timer = 0;

                stargate.markDirty();
                return;
            }

            if (!closed.locking || closed.timer++ < GateState.Closed.TICKS_PER_CHEVRON) return;

            closed.timer = 0;
            closed.locked++;

            stargate.playSound(StargateSounds.CHEVRON_LOCK);
            stargate.markDirty();

            // TODO: add energy handling.
            AddressResolveEvent.Result resolved = TEvents.handle(new AddressResolveEvent(stargate, closed.address));

            if (!(resolved instanceof AddressResolveEvent.Result.Route route)) {
                // if FAILed *OR* PASSed through all resolvers with no result and the address length >= to max chevrons of this gate, then fail
                if (resolved instanceof AddressResolveEvent.Result.Fail || closed.address.length() >= stargate.resolve(GateIdentityState.state).maxChevrons)
                    this.fail(stargate);

                return;
            }

            manager.set(stargate, new GateState.Opening(route.stargate(), true));
            manager.set(route.stargate(), new GateState.Opening(null, false));

            route.stargate().markDirty();
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
            //if (stargate.isClient()) return;

            GateState.Opening opening = stargate.state(GateState.Opening.state);

            // Adjust Bezier control points and t-mapping to linger longer near p1 and p2
            float t = (float) opening.timer / ((float) GateState.Opening.TICKS_PER_KAWOOSH * 1.25f);

            // Remap t to ease in and out, spending more time near p1 and p2
            // Use a custom curve: t' = 3t^2 - 2t^3 (smoothstep), then stretch the middle
            float tPrime = MathHelper.clamp((float) (3 * Math.pow(t, 2) - 2 * Math.pow(t, 3)), 0, 1);

            opening.kawooshHeight = (float) (
                    Math.pow(1 - tPrime, 3) * p0 +
                            3 * Math.pow(1 - tPrime, 2) * tPrime * p1 +
                            3 * (1 - tPrime) * Math.pow(tPrime, 2) * p2 +
                            Math.pow(tPrime, 3) * p3
            );

            //stargate.markDirty();

            if (opening.timer++ <= GateState.Opening.TICKS_PER_KAWOOSH || tPrime != 1) return;

            opening.kawooshHeight = 0;
            opening.timer = 0;

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

        @Override
        public void block$randomDisplayTick(Stargate stargate, World world, BlockPos pos, BlockState state, Random random) {
            if (random.nextInt(100) < 5) {
                world.playSound(null, pos, StargateSounds.WORMHOLE_LOOP, SoundCategory.BLOCKS);
            }
        }

        @Override
        public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
            if (world.isClient()) return;
            if (ServerLifecycleHooks.get().getTicks() % GateState.Open.TELEPORT_FREQUENCY != 0) return;

            GateIdentityState identityState = stargate.state(GateIdentityState.state);
            Direction facing = HorizontalBlockBehavior.getFacing(state);

            Box box = identityState.forDirection(facing).offset(pos);

            GateState.Open open = stargate.state(GateState.Open.state);
            List<Entity> entities = world.getOtherEntities(null, box, e -> e.isAlive() && !e.isSpectator());

            for (Entity e : entities) {
                if (e instanceof LivingEntity living)
                    tryTeleportFrom(stargate, open, living);
            }
        }

        public void tryTeleportFrom(Stargate stargate, GateState.Open open, LivingEntity entity) {
            if (!(entity instanceof TeleportableEntity holder))
                return;

            Stargate target = open.target;

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

//            // FIXME uh oh! this sucks!
//            double yOffset = 0;
//            for (int y = 1; y <= 5; y++) {
//                boolean bottomIsAir = targetWorld.getBlockState(targetBlockPos.up(y)).isAir();
//                boolean belowIsAir = targetWorld.getBlockState(targetBlockPos.up(y - 1)).isAir();
//                if (!bottomIsAir || !belowIsAir) {
//                    yOffset = y;
//                    break;
//                }
//            }

            entity.getWorld().playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
            targetWorld.playSound(null, targetBlockPos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

            // Check for blocks above the target position and adjust the Y offset accordingly
            // Retain entity velocity but reorient it towards the target stargate
            Vec3d velocity = entity.getVelocity();
            Vec3d direction = targetBlockPos.toCenterPos().subtract(pos.toCenterPos()).normalize();

            double speed = velocity.length();
            Vec3d newVelocity = direction.multiply(speed);

            TeleportUtil.teleport(entity, targetWorld,
                    targetBlockPos.toCenterPos().add(offset)/*.add(0, yOffset, 0)*/,
                    target.facing().asRotation()
            );

            entity.setVelocity(newVelocity);
            holder.stargate$setTicks(GateState.Open.TELEPORT_DELAY);
        }
    }
}
