package dev.amble.stargate.api.v3.behavior;

import dev.amble.stargate.api.v3.Stargate;
import dev.amble.stargate.api.v3.event.StargateEvents;
import dev.amble.stargate.api.v3.event.StargateTpEvent;
import dev.amble.stargate.api.v3.event.block.StargateBlockEvents;
import dev.amble.stargate.api.v3.state.iris.IrisState;
import dev.amble.stargate.api.v3.state.iris.ClientIrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.TBehavior;
import dev.drtheo.yaar.state.TState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IrisBehavior implements TBehavior, StargateEvents, StargateEvents.State, StargateBlockEvents {

    // TODO: IoC this so we dont have to subscribe to such a shitty event
    @Override
    public void onStateAdded(Stargate stargate, TState<?> state) {
        if (state instanceof IrisState && stargate.isClient() && !stargate.hasState(ClientIrisState.state))
            stargate.addState(new ClientIrisState());
    }

    @Override
    public StargateTpEvent.Result onGateTp(Stargate from, Stargate to, LivingEntity living) {
        boolean open = to.state(IrisState.state).open;

        if (open)
            return StargateTpEvent.Result.PASS;

        living.damage(to.world().getDamageSources().inWall(), Integer.MAX_VALUE);
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
}
