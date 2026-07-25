package dev.drtheo.ecs.state;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for all {@link TState}s.
 * @author DrTheodor (DrTheo_)
 */
public abstract class TAbstractStateRegistry {

    private boolean frozen;

    /**
     * A publicly accessible flag used for {@link TState} debugging.
     * @apiNote currently only used for {@link StateResolveError} handling.
     */
    public static boolean debug;

    private final List<TState.Type<?>> comps = new ArrayList<>();
    private final Map<ResourceLocation, TState.Type<?>> idToHolder = new HashMap<>();

    /**
     * Creates a new {@link TStateContainer.ArrayBacked} state holder if the registry is frozen.
     *
     * @return new {@link TStateContainer.ArrayBacked} state holder.
     * @throws IllegalStateException if the registry was not frozen yet.
     */
    @Contract(pure = true)
    public TStateContainer createArrayHolder() {
        if (!frozen)
            throw new IllegalStateException("Registry not frozen yet!");

        return new TStateContainer.ArrayBacked(comps.size());
    }

    /**
     * Registers a new {@link TState} by its {@link TState.Type}.
     *
     * @param type the {@link TState}'s type.
     * @throws IllegalStateException if the registry is already frozen.
     */
    public void register(TState.Type<?> type) {
        type.index = comps.size();
        type.registry = this;

        comps.add(type);
        this.add(type);
    }

    /**
     * Adds a registered {@link TState} by its {@link TState.Type}.
     *
     * @param type the {@link TState}'s type.
     * @throws IllegalStateException if the registry is already frozen.
     */
    public void add(TState.Type<?> type) {
        if (frozen)
            throw new IllegalStateException("Registry already frozen");

        idToHolder.put(type.id(), type);

        //noinspection ResultOfMethodCallIgnored
        type.verifyIndex();
    }

    /**
     * Gets a registered {@link TState.Type} by its index.
     *
     * @param index the registered state type's index.
     * @return the found {@link TState}'s type or {@code null}.
     *
     * @implNote the index may very each run, so make sure to not use constants.
     */
    @Contract(pure = true)
    public @Nullable TState.Type<?> get(int index) {
        return comps.get(index);
    }

    /**
     * Gets a registered {@link TState.Type} by its {@link ResourceLocation}.
     *
     * @param id the state's {@link ResourceLocation}.
     * @return the found {@link TState}'s type or {@code null}.
     */
    @Contract(pure = true)
    public @Nullable TState.Type<?> get(ResourceLocation id) {
        return idToHolder.get(id);
    }

    /**
     * @return the amount of registered {@link TState.Type}s.
     */
    @Contract(pure = true)
    public int size() {
        return comps.size();
    }

    /**
     * Freezes the registry.
     */
    @Contract(pure = true)
    public void freeze() {
        frozen = true;
    }
}