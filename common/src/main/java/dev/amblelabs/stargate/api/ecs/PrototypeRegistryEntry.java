package dev.amblelabs.stargate.api.ecs;

import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.drtheo.ecs.state.TAbstractStateRegistry;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record PrototypeRegistryEntry(ResourceKey<PrototypeRegistryEntry> key, Map<ResourceLocation, CompoundTag> states) {

    public void make(TAbstractStateRegistry registry, TStateContainer container, boolean isClient, boolean mark) {
        for (Map.Entry<ResourceLocation, CompoundTag> entry : states.entrySet()) {
            TState.Type<?> type = registry.get(entry.getKey());

            if (!(type instanceof TState.NbtBacked<?> serializable))
                return;

            TState<?> state = serializable.decode(entry.getValue(), isClient);
            container.addState(state);
        }

        if (mark)
            container.addState(new PrototypeIdentityState(this));
    }

    public void make(TAbstractStateRegistry registry, TStateContainer container, boolean isClient) {
        this.make(registry, container, isClient, true);
    }
}
