package dev.amble.modkit.api;

import dev.amble.lib.util.LazyObject;
import net.minecraft.block.Block;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BoatTypeRegistry {

    private static Item[] TYPE2ITEM;

    private static boolean frozen = false;

    private static final List<Entry> entries = new ArrayList<>();

    public static LazyBoatType register(Identifier id, Item item, Block block) {
        if (frozen)
            throw new IllegalStateException("Attempted to register a boat in a frozen registry");

        Entry entry = new Entry(id, item, block);
        entries.add(entry);

        return entry.lazyGetter();
    }

    public static Item getItem(BoatEntity.Type type) {
        return TYPE2ITEM[type.ordinal()];
    }

    public static void freeze() {
        frozen = true;
        TYPE2ITEM = ((CustomBoatTypes) (Object) BoatEntity.Type.OAK).amble$recalc(entries);
    }

    public interface CustomBoatTypes {
        Item[] amble$recalc(List<Entry> entries);
    }

    public record Entry(Identifier id, Item item, Block block) {

        public String getPath() {
            return id.getNamespace() + "/" + id.getPath();
        }

        public String getEnumName() {
            return id.getPath().toUpperCase();
        }

        public LazyBoatType lazyGetter() {
            return new LazyBoatType(this);
        }
    }

    public static class LazyBoatType extends LazyObject<BoatEntity.Type> implements ABoatType {

        public LazyBoatType(Entry entry) {
            super(() -> BoatEntity.Type.getType(entry.getEnumName()), BoatEntity.Type.OAK);
        }
    }
}
