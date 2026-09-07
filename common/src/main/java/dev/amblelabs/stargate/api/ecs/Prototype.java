package dev.amblelabs.stargate.api.ecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.lib.api.ecs.ImmutableTStateContainer;
import dev.amblelabs.stargate.api.StargateAPI;
import dev.amblelabs.stargate.common.impl.ecs.state.PrototypeIdentityState;
import dev.amblelabs.stargate.common.lib.StargateEcs;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.drtheo.ecs.state.TAbstractStateRegistry;
import dev.drtheo.ecs.state.TState;
import dev.drtheo.ecs.state.TStateContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record Prototype(Map<ResourceLocation, CompoundTag> states, TStateContainer staticStates, Optional<ResourceLocation> extending) {

    public void mark(TStateContainer container) {
        ResourceLocation loc = Objects.requireNonNull(XplatAbstractions.INSTANCE.getPrototypeRegistry().getKey(this));
        container.addState(new PrototypeIdentityState(loc, this));
    }

    public void make(ResourceLocation self, TAbstractStateRegistry registry, TStateContainer container, NbtDeserializer.Context context) {
        for (Map.Entry<ResourceLocation, CompoundTag> entry : states.entrySet()) {
            TState.Type<?> type = registry.get(entry.getKey());

            if (type == null) {
                StargateAPI.LOGGER.error("No state registered under {}", entry.getKey());
                continue;
            }

            if (!(type instanceof NbtState.Type<?> serializable) || container.hasState(type))
                continue;

            TState<?> state = serializable.decode(entry.getValue(), context);
            container.addState(state);
        }

        this.extending.ifPresent(prototypeId -> {
            if (self.equals(prototypeId)) return;
            Prototype entry = XplatAbstractions.INSTANCE.getPrototypeRegistry().get(prototypeId);

            if (entry == null) {
                StargateAPI.LOGGER.error("Can't extend {} for prototype {}: doesn't exist", prototypeId, self);
                return;
            }

            entry.make(prototypeId, registry, container, context);
        });
    }

    private static TStateContainer map2Container(Map<ResourceLocation, CompoundTag> map) {
        TStateContainer container = StargateEcs.StaticStates.createArrayHolder();

        for (Map.Entry<ResourceLocation, CompoundTag> entry : map.entrySet()) {
            TState.Type<?> type = StargateEcs.StaticStates.get(entry.getKey());

            if (!(type instanceof NbtState.Type<?> serializable))
                continue;

            container.addState(serializable.fromNbt(entry.getValue(), NbtDeserializer.Context.forLoad().get()));
        }

        return new ImmutableTStateContainer(container);
    }

    public static final Codec<Prototype> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(ResourceLocation.CODEC, CompoundTag.CODEC).fieldOf("states").forGetter(Prototype::states),
                Codec.unboundedMap(ResourceLocation.CODEC, CompoundTag.CODEC).optionalFieldOf("static").xmap(
                        optional -> optional.map(Prototype::map2Container).orElse(ImmutableTStateContainer.EMPTY),
                        optional -> Optional.empty()
                ).forGetter(Prototype::staticStates),
                ResourceLocation.CODEC.optionalFieldOf("extends").forGetter(Prototype::extending)
        ).apply(instance, Prototype::new));
}
