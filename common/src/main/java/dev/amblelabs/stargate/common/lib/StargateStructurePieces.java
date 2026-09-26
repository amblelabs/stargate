package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.BuriedStargatePieces;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public class StargateStructurePieces {

    private static final XplatRegistrar<StructurePieceType> REGISTRAR = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.STRUCTURE_PIECE);

    public static void register() {
        REGISTRAR.registerAll();
    }

    public static final Supplier<StructurePieceType> BURIED_STARGATE = piece("buried_stargate", BuriedStargatePieces.BuriedStargatePiece::new);

    @SuppressWarnings("SameParameterValue")
    private static Supplier<StructurePieceType> piece(String name, StructurePieceType.ContextlessType type) {
        return REGISTRAR.register(name, () -> type);
    }
}
