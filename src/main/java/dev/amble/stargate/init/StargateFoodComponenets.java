package dev.amble.stargate.init;


import net.minecraft.item.FoodComponent;

public class StargateFoodComponenets {

    public static final FoodComponent TOAST = new FoodComponent.Builder()
            .hunger(2)
            .saturationModifier(0.1f)
            .snack()
            .build();
}
