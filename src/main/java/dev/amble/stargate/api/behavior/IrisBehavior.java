package dev.amble.stargate.api.behavior;

import dev.amble.stargate.api.ServerStargate;
import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.block.StargateBlockUseEvents;
import dev.amble.stargate.api.event.tp.StargateTpEvent;
import dev.amble.stargate.api.event.block.StargateBlockTickEvents;
import dev.amble.stargate.api.event.tp.StargateTpEvents;
import dev.amble.stargate.api.state.IrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IrisBehavior implements TBehavior, StargateTpEvents, StargateBlockTickEvents, StargateBlockUseEvents {

    public static IrisBehavior INSTANCE = null;

    public IrisBehavior() {
        INSTANCE = this;
    }

    @Override
    public StargateTpEvent.Result onGateTp(ServerStargate from, ServerStargate to, LivingEntity living) {
        IrisState iris = to.state(IrisState.state);

        if (iris.open)
            return StargateTpEvent.Result.PASS;

        World targetWorld = to.world();

        living.damage(targetWorld.getDamageSources().inWall(), Integer.MAX_VALUE);
        targetWorld.playSound(null, to.pos(), StargateSounds.IRIS_HIT, SoundCategory.BLOCKS);

        this.damage(IrisDamageCtx.create().generic(to));
        return StargateTpEvent.Result.DENY;
    }

    @Override
    public void block$tick(Stargate stargate, StargateBlockEntity entity, World world, BlockPos pos, BlockState state) {
        if (world.isClient())
            return;

        IrisState irisState = stargate.state(IrisState.state);
        boolean newState = irisState.open;

        // Play sound only when IRIS state changes
        if (irisState.prevIrisState != newState) {
            SoundEvent sound = newState ? StargateSounds.IRIS_CLOSE : StargateSounds.IRIS_OPEN;
            world.playSound(null, pos, sound, SoundCategory.BLOCKS);

            irisState.prevIrisState = newState;
            stargate.markDirty();
        }
    }

    @Override
    public boolean onUse(Stargate stargate, StargateBlockEntity blockEntity, PlayerEntity player, World world, BlockState state, BlockPos pos, Hand hand, BlockHitResult blockHit) {
        if (world.isClient) return false;

        ItemStack heldItem = player.getStackInHand(hand);
        if (!heldItem.isEmpty()) return false;

        IrisState s = stargate.state(IrisState.state);
        s.open = !s.open;

        stargate.markDirty();
        return true;
    }

    public void damage(IrisDamageCtx ctx) {
        IrisState iris = ctx.stargate.state(IrisState.state);
        this.damage(ctx, iris);
    }

    protected void damage(IrisDamageCtx ctx, IrisState iris) {
        if ((iris.durability -= ctx.damage) <= 0) {
            iris.tier.onBroken(ctx);
            ctx.stargate.removeState(iris);
        }

        ctx.stargate.markDirty();
    }

    public abstract static class IrisDamageCtx {

        private final Stargate stargate;
        private final int damage;

        private IrisDamageCtx(Stargate stargate, int damage) {
            this.stargate = stargate;
            this.damage = damage;
        }

        public Stargate stargate() {
            return stargate;
        }

        public int getDamage() {
            return damage;
        }

        public static Builder create(int damage) {
            return new Builder(damage);
        }

        public static Builder create() {
            return create(1);
        }

        public static class Builder {

            private final int damage;

            public Builder(int damage) {
                this.damage = damage;
            }

            public Generic generic(Stargate stargate) {
                return new Generic(stargate, damage);
            }

            public Impact impact(Stargate from, Stargate to, LivingEntity entity) {
                return new Impact(from, to, entity, damage);
            }
        }

        public static class Generic extends IrisDamageCtx {

            private Generic(Stargate stargate, int damage) {
                super(stargate, damage);
            }
        }

        public static class Impact extends IrisDamageCtx {

            private final Stargate from;
            private final LivingEntity entity;

            private Impact(Stargate from, Stargate to, LivingEntity entity, int damage) {
                super(to, damage);

                this.from = from;
                this.entity = entity;
            }

            public Stargate from() {
                return from;
            }

            public LivingEntity by() {
                return entity;
            }
        }
    }
}
