package dev.amble.stargate.effect;

import dev.amble.stargate.init.StargateAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpacialDynamicEffect extends StatusEffect {

    public static final UUID ATTRIBUTE_ID = UUID.fromString("6d279977-e573-41ec-8f57-7d9ac97b561e");

    public SpacialDynamicEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x0000000);

        this.addAttributeModifier(StargateAttributes.SPACIAL_RESISTANCE, ATTRIBUTE_ID.toString(), 10, EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) { }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) { }
}
