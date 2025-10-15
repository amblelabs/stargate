package dev.amble.stargate.block;

import dev.amble.lib.block.ABlock;
import dev.amble.lib.block.ABlockSettings;
import dev.amble.lib.block.behavior.horizontal.HorizontalBlockBehavior;
import dev.amble.stargate.api.v2.GateKernelRegistry;
import dev.amble.stargate.block.stargates.OrlinGateBlock;
import dev.amble.stargate.init.StargateBlocks;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.common.Scheduler;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ToasterBlock extends ABlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5, 1, 5, 11, 7, 11);

    public ToasterBlock(ABlockSettings settings) {
        super(settings, HorizontalBlockBehavior.withPlacement(true));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

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

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.PASS;

        ItemStack stack = player.getStackInHand(hand);

        if (stack.isEmpty()) {
            Direction facing = HorizontalBlockBehavior.getFacing(state);
            BlockPos[] positions = facing == Direction.NORTH || facing == Direction.SOUTH
                    ? POSITIONS_NORTH : POSITIONS_EAST;

            boolean valid = true;
            for (int i = 0; i < positions.length; i++) {
                if (world.getBlockState(positions[i]).getBlock() != requiredBlocks[i]) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                for (BlockPos clearPos : positions) {
                    world.setBlockState(clearPos, Blocks.AIR.getDefaultState());
                }
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
                OrlinGateBlock.setBlockAndCreateStargate(world, pos.down(), GateKernelRegistry.ORLIN, facing);
            }

            return ActionResult.SUCCESS;
        }

        if (stack.getItem() == Items.BREAD) {
            handleItem(world, pos, stack, StargateItems.TOAST, 10);
            return ActionResult.SUCCESS;
        } else if (stack.getItem() == StargateItems.TOAST) {
            handleItem(world, pos, stack, StargateItems.BURNT_TOAST, 10);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private void handleItem(World world, BlockPos pos, ItemStack stack, Item newItem, int delay) {
        this.onActivate(world, pos, stack);

        Scheduler.get().runTaskLater(() -> {
            Vec3d spawnPosition = pos.toCenterPos().add(0, 0.4, 0);
            ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z,
                    new ItemStack(newItem));

            world.spawnEntity(toastEntity);
            world.playSound(
                    null,
                    pos,
                    StargateSounds.TOASTER,
                    SoundCategory.BLOCKS,
                    0.75F,
                    1.0F
            );
            world.playSound(
                    null,
                    pos,
                    StargateSounds.DING,
                    SoundCategory.BLOCKS,
                    1.0F,
                    1.0F
            );
        }, TaskStage.endWorldTick((ServerWorld) world), TimeUnit.SECONDS, delay);

    }

    private void onActivate(World world, BlockPos pos, ItemStack stack) {
        stack.decrement(1);

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER,
                SoundCategory.BLOCKS,
                1.0F,
                1.5F
        );

        world.playSound(
                null,
                pos,
                StargateSounds.TOASTER_ACTIVE,
                SoundCategory.BLOCKS,
                1.0F,
                1.0F
        );

    }
}