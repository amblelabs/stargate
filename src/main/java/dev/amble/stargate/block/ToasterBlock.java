package dev.amble.stargate.block;

import dev.amble.lib.block.ABlockSettings;
import dev.amble.stargate.init.StargateItems;
import dev.amble.stargate.init.StargateSounds;
import dev.drtheo.scheduler.api.TimeUnit;
import dev.drtheo.scheduler.api.client.ClientScheduler;
import dev.drtheo.scheduler.api.common.TaskStage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
        boolean isSneaking = ctx.getPlayer() != null && ctx.getPlayer().isSneaking();

        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
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
                ClientScheduler.get().runTaskLater(() -> {
                    ItemStack toastItem = new ItemStack(StargateItems.TOAST);
                    toastItem.setCount(1);
                    Vec3d spawnPosition = Vec3d.ofCenter(pos).add(0, 0.4, 0);
                    ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, toastItem);
                    world.spawnEntity(toastEntity);
                    world.playSound(
                            null,
                            pos,
                            StargateSounds.TOASTER,
                            net.minecraft.sound.SoundCategory.BLOCKS,
                            1.0F,
                            1.0F
                    );
                }, TimeUnit.SECONDS, 10);

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
                ClientScheduler.get().runTaskLater(() -> {
                    ItemStack toastItem = new ItemStack(StargateItems.BURNT_TOAST);
                    toastItem.setCount(1);
                    Vec3d spawnPosition = Vec3d.ofCenter(pos).add(0, 0.4, 0);
                    ItemEntity toastEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, toastItem);
                    world.spawnEntity(toastEntity);
                    world.playSound(
                            null,
                            pos,
                            StargateSounds.TOASTER,
                            net.minecraft.sound.SoundCategory.BLOCKS,
                            1.0F,
                            1.0F
                    );
                }, TimeUnit.SECONDS, 10);

                return ActionResult.SUCCESS;
            }

        }
        return ActionResult.PASS;
}

}