package dev.amble.stargate.block.stargates;

import dev.amble.stargate.block.AbstractStargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PegasusGateBlock extends AbstractStargateBlock {
    public PegasusGateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StargateBlockEntity(StargateBlockEntities.PEGASUS_STARGATE, pos, state);
    }
}
