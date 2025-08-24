package dev.amble.stargate.block.stargates;

import dev.amble.stargate.api.kernels.impl.OrlinGateKernel;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.api.v2.Stargate;
import dev.amble.stargate.block.AbstractStargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OrlinGateBlock extends AbstractStargateBlock {
    public OrlinGateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StargateBlockEntity(StargateBlockEntities.ORLIN_STARGATE, pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
            if (!be.hasStargate()) return super.getOutlineShape(state, world, pos, context);
            Stargate gate = be.gate().get();
            if (gate.kernel() instanceof OrlinGateKernel) {
                return rotateShape(Direction.SOUTH, state.get(FACING), makeOrlinShape());
            }
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (world.getBlockEntity(pos) instanceof StargateBlockEntity be) {
            if (!be.hasStargate()) return  super.getCollisionShape(state, world, pos, context);
            Stargate gate = be.gate().get();
            if (gate.kernel() instanceof OrlinGateKernel) {
                return rotateShape(Direction.SOUTH, state.get(FACING), makeOrlinShape());
            }
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    public VoxelShape makeOrlinShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0F, 0.0F, 0.0F, 16.0F / 16, 8.0F / 16f, 16.0F / 16f), BooleanBiFunction.OR);

        return shape;
    }

    // This is Orlin gate specific. - Loqor
    public static void setBlockAndCreateStargate(World world, BlockPos pos, GateKernelRegistry.KernelCreator kernel, Direction facing) {
        if (world.isClient()) return;
        BlockState state = StargateBlocks.ORLIN_STARGATE.getDefaultState().with(AbstractStargateBlock.FACING, facing);
        world.setBlockState(pos, state, 3);
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof StargateBlockEntity stargateBe) {
            stargateBe.onPlacedWithKernel(world, pos, kernel);
            world.playSound(null, pos, StargateSounds.DING, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
}
