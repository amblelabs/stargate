package dev.amblelabs.stargate.common.lib;

import dev.amblelabs.stargate.common.worldgen.BuriedStargatePieces;
import dev.amblelabs.stargate.xplat.XplatAbstractions;
import dev.amblelabs.stargate.xplat.XplatRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public class StargateStructurePieces {

    private static final XplatRegister<StructurePieceType> REGISTER = XplatAbstractions.INSTANCE.createRegister(BuiltInRegistries.STRUCTURE_PIECE);

    public static void register() {
        REGISTER.registerAll();
    }

    public static final Supplier<StructurePieceType> BURIED_STARGATE = piece("buried_stargate", BuriedStargatePieces.BuriedStargatePiece::new);

    @SuppressWarnings("SameParameterValue")
    private static Supplier<StructurePieceType> piece(String name, StructurePieceType.ContextlessType type) {
        return REGISTER.register(name, () -> type);
    }
}
