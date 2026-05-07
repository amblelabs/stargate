package dev.amblelabs.stargate.api.ecs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.amblelabs.stargate.common.lib.StargateRegistries;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record NbtPrototype(ResourceLocation loc, Map<ResourceLocation, CompoundTag> states) {

    public PrototypeRegistryEntry build(Registry<PrototypeRegistryEntry> registry) {
        return new PrototypeRegistryEntry(ResourceKey.create(StargateRegistries.PROTOTYPE, loc), states);
    }

    public static NbtPrototype tryDeserialize(ResourceLocation loc, JsonObject object) throws IllegalStateException {
        JsonObject states = object.getAsJsonObject("states");
        Map<ResourceLocation, CompoundTag> deserialized = new HashMap<>();

        for (Map.Entry<String, JsonElement> elem : states.entrySet()) {
            ResourceLocation stateLoc = ResourceLocation.parse(elem.getKey());
            DataResult<CompoundTag> tagResult = CompoundTag.CODEC.parse(JsonOps.INSTANCE, elem.getValue());

            // TODO: make it so it doesnt explode if just one state is fucked
            CompoundTag tag = tagResult.getOrThrow();

            deserialized.put(stateLoc, tag);
        }

        return new NbtPrototype(loc, deserialized);
    }
}
