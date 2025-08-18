package dev.amble.stargate.effect;

import dev.amble.stargate.init.StargateStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

/**
 * Represents the Ancient Gene Therapy effect, which is a status effect that grants
 * the ability to use Ancient technology and provides immunity to certain effects.
 */
public class AncientGeneTherapyEffect extends StatusEffect {

    public AncientGeneTherapyEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x8fbaff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public static boolean isAncientified(LivingEntity entity) {
        return entity.hasStatusEffect(StargateStatusEffects.ANCIENT_GENE_THERAPY);
    }
}
