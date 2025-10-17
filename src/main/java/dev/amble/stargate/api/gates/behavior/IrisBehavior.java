package dev.amble.stargate.api.gates.behavior;

import dev.amble.stargate.api.gates.Stargate;
import dev.amble.stargate.api.gates.event.init.StargateLoadedEvents;
import dev.amble.stargate.api.gates.event.init.StargateUpdateEvents;
import dev.amble.stargate.api.gates.event.tp.StargateTpEvent;
import dev.amble.stargate.api.gates.event.block.StargateBlockEvents;
import dev.amble.stargate.api.gates.event.tp.StargateTpEvents;
import dev.amble.stargate.api.gates.state.iris.IrisState;
import dev.amble.stargate.api.gates.state.iris.ClientIrisState;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IrisBehavior implements TBehavior, StargateTpEvents, StargateBlockEvents, StargateLoadedEvents, StargateUpdateEvents {

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
}
