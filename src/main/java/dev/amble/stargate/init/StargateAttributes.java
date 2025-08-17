package dev.amble.stargate.init;

import dev.amble.lib.container.RegistryContainer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class StargateAttributes implements RegistryContainer<EntityAttribute> {

    public static final EntityAttribute SPACIAL_RESISTANCE = of("attribute.stargate.spacial_resistance", 0, 0, 100);

    private static EntityAttribute of(String translation, double fallback, double min, double max) {
        return new ClampedEntityAttribute(translation, fallback, min, max).setTracked(true);
    }

    @Override
    public Class<EntityAttribute> getTargetClass() {
        return EntityAttribute.class;
    }

    @Override
    public Registry<EntityAttribute> getRegistry() {
        return Registries.ATTRIBUTE;
    }

    @Override
    public void postProcessField(Identifier identifier, EntityAttribute value, Field field) {

    }
}
