package dev.amblelabs.stargate.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.amblelabs.stargate.api.ecs.Prototype;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.behavior.ShapeBehavior;
import dev.amblelabs.stargate.common.items.StargateBlockItem;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class StargateFeature extends Feature<StargateFeature.Configuration> {

    public StargateFeature() {
        super(Configuration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        RandomSource random = context.random();
        Direction facing = Direction.values()[2 + random.nextInt(4)];

        if (!context.config().force() && !ShapeBehavior.INSTANCE.stargate$prePlace(facing, level, pos))
            return false;

        FluidState fluidState = level.getFluidState(pos);

        // TODO: use a proper BlockState resolver
        BlockState placedState = StargateBlocks.STARGATE.get().defaultBlockState().setValue(StargateBlock.FACING, facing)
                .setValue(StargateBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);

        level.setBlock(pos, placedState, Block.UPDATE_CLIENTS);

        if (!(level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity)) return false;

        Prototype entry = StargateBlockItem.pickRandomPrototype(random).value();
        Stargate stargate = ServerStargateNetwork.get(level.getLevel()).create(entry);

        blockEntity.setStargate(stargate);

        StargateBlockEvents.Lifecycle.place(stargate, blockEntity, level.getBlockState(pos), level, pos);

        stargate.setChanged(); // force sync
        return true;
    }

    public record Configuration(boolean force) implements FeatureConfiguration {

        public static final Codec<Configuration> CODEC = RecordCodecBuilder.<Configuration>mapCodec(builder ->
                builder.group(Codec.BOOL.optionalFieldOf("force", false).forGetter(Configuration::force))
                        .apply(builder, Configuration::new)).codec();
    }
}
