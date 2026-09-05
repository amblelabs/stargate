package dev.amblelabs.stargate.common.worldgen;

import com.mojang.serialization.Codec;
import dev.amblelabs.stargate.api.ecs.PrototypeRegistryEntry;
import dev.amblelabs.stargate.api.ecs.event.StargateBlockEvents;
import dev.amblelabs.stargate.api.stargate.ServerStargateNetwork;
import dev.amblelabs.stargate.api.stargate.Stargate;
import dev.amblelabs.stargate.common.blocks.StargateBlock;
import dev.amblelabs.stargate.common.blocks.StargateBlockEntity;
import dev.amblelabs.stargate.common.impl.ecs.behavior.ShapeBehavior;
import dev.amblelabs.stargate.common.lib.StargateBlocks;
import dev.amblelabs.stargate.xplat.IXplatAbstractions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Predicate;

public class StargateFeature extends Feature<NoneFeatureConfiguration> {

    public StargateFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();

        RandomSource random = context.random();
        Direction facing = Direction.values()[2 + random.nextInt(4)];

        boolean success = ShapeBehavior.INSTANCE.stargate$prePlace(facing, level, pos);
        if (!success) return false;

        FluidState fluidState = level.getFluidState(pos);

        // TODO: use a proper BlockState resolver
        level.setBlock(pos, StargateBlocks.STARGATE.defaultBlockState().setValue(StargateBlock.FACING, facing)
                .setValue(StargateBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER), Block.UPDATE_CLIENTS);

        if (!(level.getBlockEntity(pos) instanceof StargateBlockEntity blockEntity)) return false;

        PrototypeRegistryEntry entry = IXplatAbstractions.INSTANCE.getPrototypeRegistry().stream()
                .filter(Predicate.not(PrototypeRegistryEntry::isAbstract)).findAny().orElseThrow();

        Stargate stargate = ServerStargateNetwork.get(level.getLevel()).create(entry);

        blockEntity.setStargate(stargate);

        StargateBlockEvents.notify(events -> events.stargate$place(
                stargate, blockEntity, level.getBlockState(pos), level, pos));

        stargate.setChanged(); // force sync
        return true;
    }
}
