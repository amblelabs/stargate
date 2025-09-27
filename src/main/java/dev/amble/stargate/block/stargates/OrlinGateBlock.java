package dev.amble.stargate.block.stargates;

import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.block.StargateBlock;
import dev.amble.stargate.block.entities.StargateBlockEntity;
import dev.amble.stargate.init.StargateBlockEntities;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateSounds;
import dev.amble.stargate.util.VoxelShapeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class OrlinGateBlock extends StargateBlock {

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0F, 0.0F, 0.0F, 16.0F / 16, 8.0F / 16f, 1.0f);

    public OrlinGateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StargateBlockEntity(StargateBlockEntities.ORLIN_GATE, pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, state.get(FACING), SHAPE);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, state.get(FACING), SHAPE);
    }

    // This is Orlin gate specific. - Loqor
    // this is stupid
    public static void setBlockAndCreateStargate(World world, BlockPos pos, GateKernelRegistry.Entry entry, Direction facing) {
        if (world.isClient()) return;

        BlockState state = StargateBlocks.ORLIN_GATE.getDefaultState().with(StargateBlock.FACING, facing);

        world.setBlockState(pos, state, 3);

        if (world.getBlockEntity(pos) instanceof StargateBlockEntity stargateBe) {
            stargateBe.onPlacedWithKernel(world, pos, entry);
            world.playSound(null, pos, StargateSounds.DING, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
}
