package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.BuriedStargatePieces;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.amblelabs.stargate.api.StargateAPI.modLoc;

public class StargateStructurePieces {

    public static void registerStructurePieces(BiConsumer<StructurePieceType, ResourceLocation> r) {
        for (var e : STRUCTURE_PIECES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    private static final Map<ResourceLocation, StructurePieceType> STRUCTURE_PIECES = new LinkedHashMap<>();

    public static final StructurePieceType BURIED_STARGATE = piece("buried_stargate", BuriedStargatePieces.BuriedStargatePiece::new);

    private static <T extends Structure> StructurePieceType piece(String name, StructurePieceType.ContextlessType type) {
        var id = modLoc(name);

        var old = STRUCTURE_PIECES.put(id, type);
        if (old != null) throw new IllegalArgumentException("Typo? Duplicate id " + name);

        return type;
    }
}
