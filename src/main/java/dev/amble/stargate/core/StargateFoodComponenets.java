package dev.amble.stargate.core;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class StargateFoodComponenets {


    public static final FoodComponent JELLY_BABIES = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 30, 0), 1.0f)
            .snack()
            .alwaysEdible()
            .build();
}
