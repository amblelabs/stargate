package dev.amble.stargate.item;

import dev.amble.stargate.api.GateKernelRegistry;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StargateItem extends BlockItem {

    public final GateKernelRegistry.Entry creator;

    public StargateItem(Settings settings, GateKernelRegistry.Entry creator) {
        this(settings, StargateBlocks.GENERIC_GATE, creator);
    }

    private StargateItem(Settings settings, StargateBlock block, GateKernelRegistry.Entry entry) {
        super(block, settings);
        this.creator = entry;
    }

    public void place(ServerWorld world, BlockPos pos, Direction direction) {
        BlockState state = StargateBlocks.GENERIC_GATE.getDefaultState().with(Properties.HORIZONTAL_FACING, direction);
        world.setBlockState(pos, state);

        if (world.getBlockEntity(pos) instanceof StargateBlockEntity be)
            be.onPlacedWithKernel(world, state, this.creator);
    }

    @Override
    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    public GateKernelRegistry.Entry getCreator() {
        return this.creator;
    }
}
