package dev.amble.stargate.block;

import dev.amble.lib.block.ABlock;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.behavior.base.BlockWithEntityBehavior;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.block.entities.ToasterBlockEntity;
import dev.amble.stargate.init.StargateBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings({"deprecation", "UnstableApiUsage"})
public class ToasterBlock extends ABlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(5, 1, 5, 11, 7, 11);

    private static final BlockPos[] POSITIONS_NORTH = buildPositions(Direction.NORTH);
    private static final BlockPos[] POSITIONS_EAST = buildPositions(Direction.EAST);

    private static final Block[] requiredBlocks = {
            Blocks.IRON_BLOCK, // up
            Blocks.IRON_BLOCK, // east
            Blocks.IRON_BLOCK, // west
            StargateBlocks.NAQUADAH_BLOCK, // down
            Blocks.CUT_COPPER_STAIRS, // upEast
            Blocks.CUT_COPPER_STAIRS, // upWest
            Blocks.CUT_COPPER_STAIRS, // downEast
            Blocks.CUT_COPPER_STAIRS // downWest
    };

    private static BlockPos[] buildPositions(Direction facing) {
        BlockPos zero = BlockPos.ORIGIN;

        return new BlockPos[] {
                zero.up(),
                zero.offset(facing.rotateYClockwise()),
                zero.offset(facing.rotateYCounterclockwise()),
                zero.down(),
                zero.up().offset(facing.rotateYClockwise()),
                zero.up().offset(facing.rotateYCounterclockwise()),
                zero.down().offset(facing.rotateYClockwise()),
                zero.down().offset(facing.rotateYCounterclockwise())
        };
    }

    public ToasterBlock(ABlockSettings settings) {
        super(settings,
                HorizontalBlockBehavior.withPlacement(true),
                new BlockWithEntityBehavior.Ticking(ToasterBlockEntity::new)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.PASS;

        ItemStack stack = player.getStackInHand(hand);

        if (stack.isEmpty()) {
            Direction facing = HorizontalBlockBehavior.getFacing(state);
            BlockPos[] positions = facing == Direction.NORTH || facing == Direction.SOUTH
                    ? POSITIONS_NORTH : POSITIONS_EAST;

            for (int i = 0; i < positions.length; i++) {
                if (world.getBlockState(positions[i]).getBlock() != requiredBlocks[i]) {
                    return ActionResult.PASS;
                }
            }

            for (BlockPos clearPos : positions) {
                world.breakBlock(clearPos, false);
            }

            world.setBlockState(pos, StargateBlocks.ORLIN_GATE.getDefaultState().with(HorizontalBlockBehavior.FACING, facing), Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}