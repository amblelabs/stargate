package dev.amble.stargate.api.behavior;

import dev.amble.stargate.api.ServerStargate;
import dev.amble.stargate.api.event.tp.StargateTpEvent;
import dev.amble.stargate.api.event.tp.StargateTpEvents;
import dev.amble.stargate.api.state.GateState;
import dev.amble.stargate.init.StargateAttributes;
import dev.amble.stargate.init.StargateDamages;
import dev.drtheo.yaar.behavior.TBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

public class SpacialResistanceBehavior implements TBehavior, StargateTpEvents {

    @Override
    public StargateTpEvent.Result onGateTp(ServerStargate from, ServerStargate to, LivingEntity living) {
        GateState.Open open = from.state(GateState.Open.state);

        World world = living.getWorld();
        DamageSource flow = StargateDamages.flow(world);

        if (!open.caller && !living.isInvulnerableTo(flow)) {
            EntityAttribute attribute = StargateAttributes.SPACIAL_RESISTANCE;
            EntityAttributeInstance spacialResistance = living.getAttributeInstance(attribute);

            float resistance = (float) (spacialResistance == null
                    ? attribute.getDefaultValue() : spacialResistance.getValue());

            resistance = 1 - resistance / 100;

            living.damage(flow, living.getMaxHealth() * resistance);

            // TODO: add energy conversion
            if (!living.isAlive())
                return StargateTpEvent.Result.DENY;
        }

        return StargateTpEvent.Result.PASS;
    }
}
