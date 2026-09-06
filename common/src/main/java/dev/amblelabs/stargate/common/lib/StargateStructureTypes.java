package dev.amblelabs.stargate.common.lib;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.worldgen.BuriedStargateStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateStructureTypes {

    public static void registerStructureTypes(BiConsumer<StructureType<?>, ResourceLocation> r) {
        for (var e : STRUCTURE_TYPES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, StructureType<?>> STRUCTURE_TYPES = new LinkedHashMap<>();

    public static final StructureType<?> BURIED_STARGATE = type("buried_stargate", BuriedStargateStructure.CODEC);

    private static <T extends Structure> StructureType<T> type(String name, MapCodec<T> codec) {
        var id = modLoc(name);

        StructureType<T> type = () -> codec;

        var old = STRUCTURE_TYPES.put(id, type);
        if (old != null) throw new IllegalArgumentException("Typo? Duplicate id " + name);

        return type;
    }
}
