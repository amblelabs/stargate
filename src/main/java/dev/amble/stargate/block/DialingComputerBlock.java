package dev.amble.stargate.block;

import dev.amble.lib.block.ABlock;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.StargateMod;
import dev.amble.stargate.block.entities.DialingComputerBlockEntity;
import dev.amble.stargate.util.VoxelShapeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class DialingComputerBlock extends ABlock implements BlockEntityProvider {

    public static final Identifier OPEN = StargateMod.id("block/dialing_computer/open");

    protected static final VoxelShape SHAPE = Block.createCuboidShape(8, 1, 6, 14, 12, 14);

    public DialingComputerBlock(ABlockSettings settings) {
        super(settings,
                HorizontalBlockBehavior.withPlacement(true),
                BlockWithEntityBehavior.Ticking.withInvisibleModel(DialingComputerBlockEntity::new)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, HorizontalBlockBehavior.getFacing(state), SHAPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapeUtil.rotateShapeH(Direction.NORTH, HorizontalBlockBehavior.getFacing(state), SHAPE);
    }
}
