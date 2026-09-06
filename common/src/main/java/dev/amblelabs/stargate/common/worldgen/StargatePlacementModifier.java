package dev.amblelabs.stargate.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.lib.api.util.MutableBlockPos;
import dev.amblelabs.stargate.api.mod.StargateConfig;
import dev.amblelabs.stargate.common.lib.StargatePlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.stream.Stream;

public class StargatePlacementModifier extends PlacementModifier {

    public static final MapCodec<StargatePlacementModifier> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(Heightmap.Types.CODEC.optionalFieldOf("heightmap", Heightmap.Types.OCEAN_FLOOR_WG).forGetter(placement -> placement.heightmap),
                            Codec.INT.optionalFieldOf("y_offset", 0).forGetter(placement -> placement.yOffset))
                    .apply(builder, StargatePlacementModifier::new));

    private final Heightmap.Types heightmap;
    private final int yOffset;

    public StargatePlacementModifier(Heightmap.Types heightmap, int yOffset) {
        this.heightmap = heightmap;
        this.yOffset = yOffset;
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        if (!this.isStargateChunk(context, pos)) return Stream.empty();

        WorldGenLevel level = context.getLevel();
        int y = context.getHeight(heightmap, pos.getX(), pos.getZ());

        MutableBlockPos mutablePos = new MutableBlockPos(pos.getX(), y + this.yOffset, pos.getZ());

        if (level.dimensionType().hasCeiling()) {
            BlockState stateAbove = level.getBlockState(mutablePos);

            while (mutablePos.getY() > context.getMinBuildHeight()) {
                mutablePos.below();

                BlockState state = level.getBlockState(mutablePos);
                if (!state.isAir() && state.getFluidState().isEmpty() && stateAbove.canBeReplaced() && !state.is(BlockTags.FEATURES_CANNOT_REPLACE)) {
                    mutablePos.above();
                    break;
                }

                stateAbove = state;
            }
        }

        return y > context.getMinBuildHeight() ? Stream.of(mutablePos) : Stream.empty();
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return StargatePlacementModifiers.STARGATE;
    }

    private boolean isStargateChunk(PlacementContext context, BlockPos pos) {
        final int distance = StargateConfig.server().worldGenStargateDistance();
        if (distance == 0) return false;

        final int maxDeviation = (int) Math.ceil(distance / 2f);

        int chunkX = pos.getX() / 16;
        int chunkZ = pos.getZ() / 16;

        long seed = context.getLevel().getSeed();
        Random random = new Random(chunkX * maxDeviation * chunkZ * maxDeviation * seed);

        return (random.nextInt(maxDeviation) + chunkX) % distance == 0
                && (random.nextInt(maxDeviation) + chunkZ) % distance == 0;
    }
}
