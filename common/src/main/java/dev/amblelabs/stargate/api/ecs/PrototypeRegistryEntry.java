package dev.amblelabs.stargate.api.ecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import dev.drtheo.ecs.state.TAbstractStateRegistry;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;

public record PrototypeRegistryEntry(Map<ResourceLocation, CompoundTag> states) {

    public void mark(TStateContainer container) {
        ResourceLocation loc = Objects.requireNonNull(IXplatAbstractions.INSTANCE.getPrototypeRegistry().getKey(this));
        container.addState(new PrototypeIdentityState(loc, this));
    }

    public void make(TAbstractStateRegistry registry, TStateContainer container, NbtDeserializer.Context context) {
        for (Map.Entry<ResourceLocation, CompoundTag> entry : states.entrySet()) {
            TState.Type<?> type = registry.get(entry.getKey());

            if (!(type instanceof NbtState.Type<?> serializable) || container.hasState(type))
                return;

            TState<?> state = serializable.decode(entry.getValue(), context);
            container.addState(state);
        }
    }

    public static final Codec<PrototypeRegistryEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(ResourceLocation.CODEC, CompoundTag.CODEC).fieldOf("states").forGetter(PrototypeRegistryEntry::states)
    ).apply(instance, PrototypeRegistryEntry::new));
}
