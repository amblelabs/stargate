package dev.amble.stargate.api.v4.state;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TStateRegistry {

    private static boolean frozen;
    public static boolean debug;

    private static final List<TState.Type<?>> comps = new ArrayList<>();
    private static final Map<Identifier, TState.Type<?>> idToHolder = new HashMap<>();

    public static TStateContainer createListHolder() {
        if (!frozen)
            throw new IllegalStateException("Registry not frozen yet!");

        return new TStateContainer.ListBacked(comps.size());
    }

    public static void register(TState.Type<?> type) {
        if (frozen)
            throw new IllegalStateException("Already frozen");

        type.index = comps.size();

        comps.add(type);
        idToHolder.put(type.id(), type);
    }

    public TState.Type<?> get(int index) {
        return comps.get(index);
    }

    public static TState.Type<?> get(Identifier id) {
        return idToHolder.get(id);
    }

    public static int size() {
        return comps.size();
    }

    public static void freeze() {
        frozen = true;
    }
}
