package dev.amble.stargate.block;

import dev.amble.lib.block.ABlockSettings;
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
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static net.minecraft.block.BarrelBlock.FACING;

public class ToasterBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5, 1, 5, 11, 7, 11);

    public ToasterBlock(ABlockSettings settings) {
            super(settings);
            this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH));
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (player.getMainHandStack().isEmpty()) {
                Direction facing = state.get(FACING);

                // Define relative positions and required blocks
                BlockPos[] positions = {
                        pos.up(), // up
                        pos.offset(facing.rotateYClockwise()), // east
                        pos.offset(facing.rotateYCounterclockwise()), // west
                        pos.down(), // down
                        pos.up().offset(facing.rotateYClockwise()), // upEast
                        pos.up().offset(facing.rotateYCounterclockwise()), // upWest
                        pos.down().offset(facing.rotateYClockwise()), // downEast
                        pos.down().offset(facing.rotateYCounterclockwise()) // downWest
                };
                Block[] requiredBlocks = {
                        Blocks.IRON_BLOCK, // up
                        Blocks.IRON_BLOCK, // east
                        Blocks.IRON_BLOCK, // west
                        StargateBlocks.NAQUADAH_BLOCK, // down
                        Blocks.CUT_COPPER_STAIRS, // upEast
                        Blocks.CUT_COPPER_STAIRS, // upWest
                        Blocks.CUT_COPPER_STAIRS, // downEast
                        Blocks.CUT_COPPER_STAIRS // downWest
                };

                boolean valid = true;
                for (int i = 0; i < positions.length; i++) {
                    if (world.getBlockState(positions[i]).getBlock() != requiredBlocks[i]) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    // Clear all involved blocks
                    for (BlockPos clearPos : positions) {
                        world.setBlockState(clearPos, Blocks.AIR.getDefaultState());
                    }
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
                    OrlinGateBlock.setBlockAndCreateStargate(world, pos.down(), GateKernelRegistry.ORLIN, facing);
                }
                return ActionResult.SUCCESS;
            }
            if (player.getStackInHand(hand).getItem() == Items.BREAD) {
                player.getStackInHand(hand).decrement(1);
                world.playSound(
                        null,
                        pos,
                        StargateSounds.TOASTER,
                        net.minecraft.sound.SoundCategory.BLOCKS,
                        1.0F,
                        1.5F
                );
                world.playSound(
                        null,
                        pos,
                        StargateSounds.TOASTER_ACTIVE,
                        net.minecraft.sound.SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );
                Scheduler.get().runTaskLater(() -> {
                    ItemStack toastItem = new ItemStack(StargateItems.TOAST);
                    toastItem.setCount(1);
                    Vec3d spawnPosition = Vec3d.ofCenter(pos).add(0, 0.4, 0);
                    ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, toastItem);
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
                }, TaskStage.endWorldTick((ServerWorld) world), TimeUnit.SECONDS, 10);

                return ActionResult.SUCCESS;
            }

            if (player.getStackInHand(hand).getItem() == StargateItems.TOAST) {
                player.getStackInHand(hand).decrement(1);
                world.playSound(
                        null,
                        pos,
                        StargateSounds.TOASTER,
                        net.minecraft.sound.SoundCategory.BLOCKS,
                        1.0F,
                        1.5F
                );
                world.playSound(
                        null,
                        pos,
                        StargateSounds.TOASTER_ACTIVE,
                        net.minecraft.sound.SoundCategory.BLOCKS,
                        1.0F,
                        1.0F
                );
                Scheduler.get().runTaskLater(() -> {
                    ItemStack toastItem = new ItemStack(StargateItems.BURNT_TOAST);
                    toastItem.setCount(1);
                    Vec3d spawnPosition = Vec3d.ofCenter(pos).add(0, 0.4, 0);
                    ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, toastItem);
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
                }, TaskStage.endWorldTick((ServerWorld) world), TimeUnit.SECONDS, 10);

                return ActionResult.SUCCESS;
            }

        }
        return ActionResult.PASS;
    }

}