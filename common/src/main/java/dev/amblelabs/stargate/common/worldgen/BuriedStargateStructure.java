package dev.amblelabs.stargate.common.worldgen;

import com.mojang.serialization.MapCodec;
import dev.amblelabs.stargate.common.lib.StargateStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class BuriedStargateStructure extends Structure {
    public static final MapCodec<BuriedStargateStructure> CODEC = simpleCodec(BuriedStargateStructure::new);

    public BuriedStargateStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, (structurePiecesBuilder) -> generatePieces(structurePiecesBuilder, context));
    }

    private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getBlockX(9), 90, context.chunkPos().getBlockZ(9));
        builder.addPiece(new BuriedStargatePieces.BuriedStargatePiece(blockPos));
    }

    @Override
    public StructureType<?> type() {
        return StargateStructureTypes.BURIED_STARGATE;
    }
}
