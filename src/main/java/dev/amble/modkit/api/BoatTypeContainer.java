package dev.amble.modkit.api;

import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public abstract class BoatTypeContainer implements RegistryContainer2<ABoatType> {

    protected static ABoatType register(Item item, Block block) {
        return new Pending(item, block);
    }

    record Pending(Item item, Block block) implements ABoatType {

        @Override
        public BoatEntity.Type get() {
            throw new IllegalStateException("This boat type was not registered yet!");
        }
    }

    public Class<ABoatType> getTargetClass() {
        return ABoatType.class;
    }

    @Override
    public void postProcessField(Identifier identifier, ABoatType value, Field field) {
        if (value instanceof Pending pending) {
            try {
                field.setAccessible(true);
                field.set(this, BoatTypeRegistry.register(identifier, pending.item, pending.block));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
