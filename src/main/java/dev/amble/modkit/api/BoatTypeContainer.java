package dev.amble.modkit.api;

import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public abstract class BoatTypeContainer implements RegistryContainer2<BoatTypeContainer.Holder> {

    protected static ABoatType register(Item item, Block block) {
        return new Holder(new Pending(item, block));
    }

    record Pending(Item item, Block block) implements ABoatType {

        @Override
        public BoatEntity.Type get() {
            throw new IllegalStateException("This boat type was not registered yet!");
        }

        public ABoatType register(Identifier id) {
            return BoatTypeRegistry.register(id, this.item, this.block);
        }
    }

    public static final class Holder implements ABoatType {

        ABoatType child;

        Holder(ABoatType child) {
            this.child = child;
        }

        @Override
        public BoatEntity.Type get() {
            return child.get();
        }
    }

    public Class<Holder> getTargetClass() {
        return Holder.class;
    }

    @Override
    public void postProcessField(Identifier identifier, Holder value, Field field) {
        value.child = ((Pending) value.child).register(identifier);
    }

    @Override
    public void finish() {
        BoatTypeRegistry.apply();
    }
}
