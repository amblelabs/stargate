package dev.amblelabs.stargate.common.impl.ecs.behavior;

import dev.amblelabs.stargate.api.ecs.event.StargateTpEvent;
import dev.amblelabs.stargate.api.ecs.event.StargateTpEvents;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.impl.ecs.state.GateState;
import dev.amblelabs.stargate.common.lib.StargateAttributes;
import dev.amblelabs.stargate.common.lib.StargateDamageTypes;
import dev.drtheo.ecs.behavior.TBehavior;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;

public class SpacialResistanceBehavior implements TBehavior, StargateTpEvents {

    @Override
    public StargateTpEvent.Result onGateTp(Stargate from, Stargate to, Entity entity) {
        GateState<?> state = from.state(GateState.state);

        Level level = entity.level();
        DamageSource flow = StargateDamageTypes.source(level, StargateDamageTypes.FLOW);

        if (!(state instanceof GateState.Open open) || open.caller || entity.isInvulnerableTo(flow))
            return StargateTpEvent.Result.PASS;

        if (entity instanceof LivingEntity living) {
            Holder<Attribute> attribute = StargateAttributes.SPACIAL_RESISTANCE;
            double resistance = attribute.value().getDefaultValue();

            AttributeInstance spacialResistance = living.getAttribute(attribute);

            if (spacialResistance != null)
                resistance = spacialResistance.getValue();

            entity.hurt(flow, (float) (living.getMaxHealth() * (1 - resistance / 100f)));
        } else {
            entity.kill();
        }

        // TODO: add energy conversion
        if (!entity.isAlive())
            return StargateTpEvent.Result.DENY;

        return StargateTpEvent.Result.PASS;
    }
}