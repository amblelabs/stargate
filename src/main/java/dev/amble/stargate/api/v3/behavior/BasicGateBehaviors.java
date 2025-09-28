package dev.amble.stargate.api.v3.behavior;

import dev.amble.lib.data.DirectedGlobalPos;
import dev.amble.lib.util.ServerLifecycleHooks;
import dev.amble.lib.util.TeleportUtil;
import dev.amble.stargate.api.TeleportableEntity;
import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.state.BasicGateStates;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateAttachments;
import dev.amble.stargate.init.StargateAttributes;
import dev.amble.stargate.init.StargateDamages;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.state.TState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public interface BasicGateBehaviors {

    abstract class Closed implements TBehavior {

    }

    abstract class Opening implements TBehavior, StargateEvents {

        @Override
        public void onStateChanged(Stargate stargate, TState<?> oldState, TState<?> newState) {
            if (!(newState instanceof BasicGateStates.Opening)) return;

            stargate.playSound(StargateSounds.GATE_OPEN);
        }
    }

    abstract class Open implements TBehavior, StargateEvents, StargateBlockEvents {

        public static final Box NS_DEFAULT = new Box(BlockPos.ORIGIN).expand(2, 2, 0).expand(0, 3, 0);
        public static final Box WE_DEFAULT = new Box(BlockPos.ORIGIN).expand(0, 2, 2).expand(0, 3, 0);

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

            DirectedGlobalPos targetPos = target.address().pos();

            BlockPos pos = stargate.address().pos().getPos();

            World world = entity.getWorld();
            DamageSource flow = StargateDamages.flow(world);

            if (!open.caller && !entity.isInvulnerableTo(flow)) {
                EntityAttribute attribute = StargateAttributes.SPACIAL_RESISTANCE;
                EntityAttributeInstance spacialResistance = entity.getAttributeInstance(attribute);

                float resistance = (float) (spacialResistance == null
                        ? attribute.getDefaultValue() : spacialResistance.getValue());

                resistance = 1 - resistance / 100;

                entity.damage(flow, entity.getMaxHealth() * resistance);

                // TODO: add energy conversion
                if (!entity.isAlive())
                    return;
            }

            Vec3d offset = entity.getPos().subtract(pos.toCenterPos().add(0, -0.5, 0));

            ServerWorld targetWorld = ServerLifecycleHooks.get().getWorld(targetPos.getDimension());
            BlockPos targetBlockPos = targetPos.getPos();

            if (targetWorld == null)
                return;

            double yOffset = 0;
            for (int y = 1; y <= 5; y++) {
                boolean bottomIsAir = targetWorld.getBlockState(targetBlockPos.up(y)).isAir();
                boolean belowIsAir = targetWorld.getBlockState(targetBlockPos.up(y - 1)).isAir();
                if (!bottomIsAir || !belowIsAir) {
                    yOffset = y;
                    break;
                }
            }

            world.playSound(null, pos, StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);
            targetWorld.playSound(null, entity.getBlockPos(), StargateSounds.GATE_TELEPORT, SoundCategory.BLOCKS, 1f, 1);

            // Check for blocks above the target position and adjust the Y offset accordingly

            // Retain entity velocity but reorient it towards the target stargate
            Vec3d velocity = entity.getVelocity();
            Vec3d direction = targetBlockPos.toCenterPos().subtract(pos.toCenterPos()).normalize();
            double speed = velocity.length();
            Vec3d newVelocity = direction.multiply(speed);

            // TODO: iris shit
//            if (!targetWorld.getBlockState(targetPos.getPos()).isAir() && targetWorld.getBlockState(targetPos.getPos()).get(StargateBlock.IRIS)) {
//                entity.damage(targetWorld.getDamageSources().inWall(), Integer.MAX_VALUE); // Lol
//            }

            TeleportUtil.teleport(entity, targetWorld,
                    targetBlockPos.toCenterPos().add(offset).add(0, yOffset, 0),
                    targetPos.getRotationDegrees()
            );

            //noinspection UnstableApiUsage
            entity.modifyAttached(StargateAttachments.HAS_PASSED_THROUGH_STARGATE, (bl) -> {
                if (bl == null || !bl) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 4,
                            2, false, false, true));
                }
                return true;
            });

            if (entity.canFreeze())
                entity.setFrozenTicks(100);

            entity.setVelocity(newVelocity);

            holder.stargate$setTicks(BasicGateStates.Open.TELEPORT_DELAY);
        }
    }
}
