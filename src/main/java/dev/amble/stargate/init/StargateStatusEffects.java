package dev.amble.stargate.init;

import dev.amble.lib.container.RegistryContainer;
import dev.amble.stargate.effect.AncientGeneTherapyEffect;
import dev.amble.stargate.effect.SpacialDynamicEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class StargateStatusEffects implements RegistryContainer<StatusEffect> {

    public static final StatusEffect SPACIAL_DYNAMIC = new SpacialDynamicEffect();
    public static final StatusEffect ANCIENT_GENE_THERAPY = new AncientGeneTherapyEffect();

    @Override
    public Class<StatusEffect> getTargetClass() {
        return StatusEffect.class;
    }

    @Override
    public Registry<StatusEffect> getRegistry() {
        return Registries.STATUS_EFFECT;
    }
}
