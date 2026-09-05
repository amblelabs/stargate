package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.StargatePlacementModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargatePlacementModifiers {

    public static void registerPlacementModifiers(BiConsumer<PlacementModifierType<?>, ResourceLocation> r) {
        for (var e : PLACEMENT_MODIFIERS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, PlacementModifierType<?>> PLACEMENT_MODIFIERS = new LinkedHashMap<>();

    public static final PlacementModifierType<?> STARGATE = placementModifier("stargate", () -> StargatePlacementModifier.CODEC);

    private static <T extends PlacementModifier> PlacementModifierType<T> placementModifier(String name, PlacementModifierType<T> type) {
        var id = modLoc(name);

        var old = PLACEMENT_MODIFIERS.put(id, type);
        if (old != null) throw new IllegalArgumentException("Typo? Duplicate id " + name);

        return type;
    }
}
