package dev.amble.stargate.api.behavior;

import dev.amble.stargate.api.Stargate;
import dev.amble.stargate.api.event.block.StargateBlockUseEvents;
import dev.amble.stargate.api.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.event.init.StargateUpdateEvents;
import dev.amble.stargate.api.event.tp.StargateTpEvent;
import dev.amble.stargate.api.event.block.StargateBlockTickEvents;
import dev.amble.stargate.api.event.tp.StargateTpEvents;
import dev.amble.stargate.api.state.iris.IrisState;
import dev.amble.stargate.api.state.iris.ClientIrisState;
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

public class IrisBehavior implements TBehavior, StargateTpEvents, StargateBlockTickEvents, StargateLoadedEvents, StargateUpdateEvents, StargateBlockUseEvents {

    @Override
    public StargateTpEvent.Result onGateTp(Stargate from, Stargate to, LivingEntity living) {
        boolean open = to.state(IrisState.state).open;

        if (open)
            return StargateTpEvent.Result.PASS;

        living.damage(to.world().getDamageSources().inWall(), Integer.MAX_VALUE);
        to.world().playSound(null, to.pos(), StargateSounds.IRIS_HIT, SoundCategory.BLOCKS);

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
    public void onLoaded(Stargate stargate) {
        this.checkAndAttach(stargate);
    }

    @Override
    public void onUpdate(Stargate stargate) {
        this.checkAndAttach(stargate);
    }

    private void checkAndAttach(Stargate stargate) {
        if (stargate.isClient() && stargate.hasState(IrisState.state) && !stargate.hasState(ClientIrisState.state))
            stargate.addState(new ClientIrisState());
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
}
